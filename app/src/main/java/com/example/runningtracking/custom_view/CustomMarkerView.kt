package com.androiddevs.runningappyt.other

import android.content.Context
import android.widget.TextView
import com.example.runningtracking.R
import com.example.runningtracking.ui.run.model.RunResponse
import com.example.runningtracking.utils.TrackingUtils
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

class CustomMarkerView(
    val runs: List<RunResponse>,
    c: Context,
    layoutId: Int
) : MarkerView(c, layoutId) {

    override fun getOffset(): MPPointF {
        return MPPointF(-width / 2f, -height.toFloat())
    }

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        super.refreshContent(e, highlight)
        if(e == null) {
            return
        }
        val curRunId = e.x.toInt()
        val run = runs[curRunId]

        findViewById<TextView>(R.id.tvDate).text = formatDateTime(run.createdAt)

        val avgSpeed = "${run.avgSpeed}km/h"
        findViewById<TextView>(R.id.tvAvgSpeed).text = avgSpeed

        val distanceInKm = "${run.distance / 1000f}km"
        findViewById<TextView>(R.id.tvDistance).text = distanceInKm

        findViewById<TextView>(R.id.tvDuration).text = TrackingUtils.getFormattedStopWatchTime(run.time)

        val caloriesBurned = "${run.caloriesBurned}kcal"
        findViewById<TextView>(R.id.tvCaloriesBurned).text = caloriesBurned
    }

    private fun formatDateTime(timestampStr: String): String {
        val timestamp = Instant.parse(timestampStr)
        val calendar = Calendar.getInstance().apply {
            time = Date.from(timestamp)
        }
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:MM", Locale.US)
        return dateFormat.format(calendar.time)
    }
}