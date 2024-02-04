package com.example.runningtracking.ui.tracking

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.example.runningtracking.R
import com.example.runningtracking.base.ViewBindingFragment
import com.example.runningtracking.databinding.FragTrackingBinding
import com.example.runningtracking.model.Run
import com.example.runningtracking.services.Polyline
import com.example.runningtracking.services.TrackingService
import com.example.runningtracking.services.TrackingService.Companion.ACTION_PAUSE_SERVICE
import com.example.runningtracking.services.TrackingService.Companion.ACTION_START_OR_RESUME_SERVICE
import com.example.runningtracking.services.TrackingService.Companion.ACTION_STOP_SERVICE
import com.example.runningtracking.utils.AppUtils
import com.example.runningtracking.utils.TrackingUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.round

@AndroidEntryPoint
class TrackingFrag : ViewBindingFragment<FragTrackingBinding>() {
    private val viewModel: TrackingViewModel by viewModels()
    private var isTracking = false
    private var map: GoogleMap? = null
    private var pathPoints = mutableListOf<Polyline>()
    private var curTimeInMillis = 0L
    private lateinit var menuHost: MenuHost
    private var weight = 80f

    companion object {
        const val POLYLINE_COLOR = Color.RED
        const val POLYLINE_WIDTH = 8f
        const val MAP_ZOOM = 15f
        const val CANCEL_TRACKING_DIALOG_TAG = "cancel_tracking_dialog_tag"
    }


    private fun subscribeToObservers() {
        TrackingService.isTracking.observe(viewLifecycleOwner) {
            updateTracking(it)
        }

        TrackingService.pathPoints.observe(viewLifecycleOwner) {
            pathPoints = it
            addLatestPolyline()
            moveCameraToUser()
        }

        TrackingService.timeRunInMillis.observe(viewLifecycleOwner) {
            curTimeInMillis = it
            val formattedTime = TrackingUtils.getFormattedStopWatchTime(curTimeInMillis, true)
            binding!!.tvTimer.text = formattedTime
        }
    }

    private fun toggleRun() {
        if (isTracking) {
            sendCommandToService(ACTION_PAUSE_SERVICE)
        } else {
            sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
        }
    }



    private fun updateTracking(isTracking: Boolean) {
        this.isTracking = isTracking
        menuHost.invalidateMenu()
        if (!isTracking && curTimeInMillis > 0L) {
            binding?.apply {
                btnToggleRun.text = "Start"
                btnFinishRun.visibility = View.VISIBLE
            }

        } else if (isTracking) {
            binding?.apply {
                btnToggleRun.text = "Stop"
                btnFinishRun.visibility = View.GONE
            }
        }
    }

    private fun addLatestPolyline() {
        if (pathPoints.isNotEmpty() && pathPoints.last().size > 1) {
            val preLastLatLng = pathPoints.last()[pathPoints.last().size - 2]
            val lastLatLng = pathPoints.last().last()
            val polylineOptions = PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH)
                .add(preLastLatLng)
                .add(lastLatLng)
            map?.addPolyline(polylineOptions)
        }
    }

    private fun addAllPolylines() {
        for (polyline in pathPoints) {
            val polylineOptions = PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH)
                .addAll(polyline)
            map?.addPolyline(polylineOptions)
        }
    }

    private fun moveCameraToUser() {
        if (pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()) {
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pathPoints.last().last(),
                    MAP_ZOOM
                )
            )
        }
    }

    private fun zoomToSeeWholeTrack() {
        if (pathPoints.isEmpty()) return
        if (pathPoints.all { it.isEmpty() }) return
        val bounds = LatLngBounds.Builder()
        for(polyline in pathPoints) {
            for(pos in polyline) {
                bounds.include(pos)
            }
        }

        map?.moveCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds.build(),
                binding!!.mapView.width,
                binding!!.mapView.height,
                (binding!!.mapView.height * 0.05f).toInt()
            )
        )
    }

    private fun endRunAndSaveToDb() {
        map?.snapshot { bmp ->
            var distanceInMeters = 0
            for(polyline in pathPoints) {
                distanceInMeters += TrackingUtils.calculatePolylineLength(polyline).toInt()
            }
            val avgSpeed = round((distanceInMeters / 1000f) / (curTimeInMillis / 1000f / 60 / 60) * 10) / 10f
            val caloriesBurned = ((distanceInMeters / 1000f) * weight).toInt()

            val run = Run(bmp, avgSpeed, distanceInMeters, curTimeInMillis, caloriesBurned)
            bmp?.let {
                val photo = AppUtils().getResizedBitmap(bmp, 1000)
                val file = AppUtils().convertBitmapToFile(requireContext(), "photo.jpeg", photo)
                run.apply {
                    attachmentPath = file.absolutePath
                }
            }
            viewModel.setEvent(Event.FinishRun(run))
//            Snackbar.make(
//                requireActivity().findViewById(R.id.rootView),
//                "Run saved successfully",
//                Snackbar.LENGTH_LONG
//            ).show()
            stopRun()
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        viewGroup: ViewGroup?
    ): FragTrackingBinding {
        return FragTrackingBinding.inflate(inflater, viewGroup, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        if (savedInstanceState != null){
            val cancelTrackingDialog = parentFragmentManager.findFragmentByTag(
                CANCEL_TRACKING_DIALOG_TAG) as CancelTrackingDialog?
            cancelTrackingDialog?.setYesListener {
                stopRun()
            }
        }

        menuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.toolbar_tracking_menu, menu)
            }

            override fun onPrepareMenu(menu: Menu) {
                super.onPrepareMenu(menu)
                menu.getItem(0).setVisible(isTracking)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.miCancelTracking -> {
                        showCancelTrackingDialog()
                        return true
                    }
                }
                return false
            }


        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
        binding?.apply {
            mapView.onCreate(savedInstanceState)
            mapView.getMapAsync {
                map = it
                addAllPolylines()
            }

            btnToggleRun.setOnClickListener {
                toggleRun()
            }
            btnFinishRun.setOnClickListener {
                zoomToSeeWholeTrack()
                endRunAndSaveToDb()
            }
        }

        subscribeToObservers()
    }


    private fun showCancelTrackingDialog() {
        CancelTrackingDialog().apply {
            setYesListener {
                stopRun()
            }
        }.show(parentFragmentManager, CANCEL_TRACKING_DIALOG_TAG)

    }

    private fun stopRun() {
        binding?.apply {
            tvTimer.text = "00:00:00:00"
        }
        sendCommandToService(ACTION_STOP_SERVICE)
        openFragment(R.id.action_trackingFragment_to_runFragment)
    }


    private fun sendCommandToService(action: String) {
        Intent(requireContext(), TrackingService::class.java).also {
            it.action = action
            requireContext().startService(it)
        }
    }

    override fun onResume() {
        super.onResume()
        binding?.apply {
            mapView.onResume()
        }
    }

    override fun onStart() {
        super.onStart()
        binding?.apply {
            mapView.onStart()
        }
    }

    override fun onStop() {
        super.onStop()
        binding?.apply {
            mapView.onStop()
        }
    }

    override fun onPause() {
        super.onPause()
        binding?.apply {
            mapView.onPause()
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding?.apply {
            mapView.onLowMemory()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding?.apply {
            mapView.onSaveInstanceState(outState)
        }
    }
}