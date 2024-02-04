package com.example.runningtracking.ui.statistics

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.androiddevs.runningappyt.other.CustomMarkerView
import com.example.runningtracking.R
import com.example.runningtracking.base.ViewBindingFragment
import com.example.runningtracking.databinding.FragStatisticsBinding
import com.example.runningtracking.utils.TrackingUtils
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StatisticsFrag: ViewBindingFragment<FragStatisticsBinding>() {

    private val viewModel: StatisticViewModel by viewModels()

    override fun getViewBinding(
        inflater: LayoutInflater,
        viewGroup: ViewGroup?
    ): FragStatisticsBinding {
        return FragStatisticsBinding.inflate(inflater, viewGroup, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupBarChart()
        observe()
    }

    private fun setupBarChart(){
        binding?.apply {
            barChart.xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawLabels(false)
                axisLineColor = Color.BLUE
                textColor = Color.BLUE
                setDrawGridLines(false)
            }
            barChart.axisLeft.apply {
                axisLineColor = Color.BLUE
                textColor = Color.BLUE
                setDrawGridLines(false)
            }
            barChart.axisRight.apply {
                axisLineColor = Color.BLUE
                textColor = Color.BLUE
                setDrawGridLines(false)
            }
            barChart.apply {
                legend.isEnabled = false
                contentDescription = "Average speed"
            }
        }
    }

    private fun observe() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED){
                viewModel.uiState.collect{
                    if (it.statistic != null){
                        binding?.apply {
                            tvAverageSpeed.text = "${String.format("%.2f", it.statistic.averageSpeed!! / 1000f)} Km/h"
                            tvTotalTime.text = TrackingUtils.getFormattedStopWatchTime(it.statistic.totalTime!!)
                            tvTotalCalories.text = "${it.statistic.caloriesBurned} Kcal"
                            tvTotalDistance.text =  "${String.format("%.2f", it.statistic.totalDistance!! / 1000f)} Km"
                        }
                    }

                    if (it.runs != null){
                        val allAvgSpeeds = it.runs.indices.map { i -> BarEntry(i.toFloat(), it.runs[i].avgSpeed) }
                        val bardataSet = BarDataSet(allAvgSpeeds, "Avg Speed").apply {
                            valueTextColor = Color.BLUE
                            color = ContextCompat.getColor(requireContext(), R.color.md_blue_A400)
                        }
                        binding?.apply {
                            barChart.data = BarData(bardataSet)
                            barChart.marker = null
                            barChart.invalidate()
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED){
                viewModel.effect.collect {
                    when(it){
                        is Effect.Failure -> {
                            Snackbar.make(requireView(), "Some thing went wrong", Snackbar.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        viewModel.setEvent(Event.OnResume)
        super.onResume()
    }
}