package com.example.runningtracking.ui.run

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.runningtracking.R
import com.example.runningtracking.base.ViewBindingFragment
import com.example.runningtracking.databinding.FragRunBinding
import com.example.runningtracking.ui.run.model.RunResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RunFrag : ViewBindingFragment<FragRunBinding>() {

    private val viewModel: RunViewModel by viewModels()

    private val perms = arrayListOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
    )


    private val permReqLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        val isGranted = perms.entries.all {
            it.value
        }

        if (isGranted) {

        }
    }

    override fun getViewBinding(inflater: LayoutInflater, viewGroup: ViewGroup?): FragRunBinding {
        return FragRunBinding.inflate(inflater, viewGroup, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.apply {
            fab.setOnClickListener {
                openFragment(R.id.action_runFragment_to_trackingFragment)
            }
        }
        requestPermissions()

        observe()
    }

    private fun observe() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED){
                viewModel.uiState.collect {
                    if (!it.runResponse.isNullOrEmpty()){
                        setUpAdapterView(it.runResponse!!)
                    }
                }
            }
        }
    }

    private fun setUpAdapterView(runResponse: List<RunResponse>) {
        binding?.apply {
            rvRuns.adapter = RunAdapter(runResponse)
        }
    }

    private fun hasPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            perms.add(Manifest.permission.POST_NOTIFICATIONS)
        }
        return perms.all {
            ActivityCompat.checkSelfPermission(
                requireContext(),
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermissions() {
        if (hasPermission()) {
            return
        }
        permReqLauncher.launch(perms.toTypedArray())
        permReqLauncher.launch(arrayOf(
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        ))
    }

    override fun onResume() {
        super.onResume()
        viewModel.setEvent(Event.OnResume)
    }

}