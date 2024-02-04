package com.example.runningtracking.ui.run

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.runningtracking.databinding.ItemRunTrackingBinding
import com.example.runningtracking.ui.run.model.RunResponse
import com.example.runningtracking.utils.TrackingUtils
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Calendar
import java.util.Date
import java.util.Locale

class RunAdapter(private val items: List<RunResponse>) : RecyclerView.Adapter<RunAdapter.Holder>() {
    inner class Holder(private val binding: ItemRunTrackingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int) {
            val item = items[position]
            binding.apply {
                tvCalories.text = "${item.caloriesBurned} KCAL"
                tvDate.text = formatDateTime(item.createdAt)
                tvTime.text = TrackingUtils.getFormattedStopWatchTime(item.time)
                tvDistance.text = "${item.distance / 1000f} Km"
                tvAvgSpeed.text = "${item.avgSpeed} Km/h"
                Glide.with(binding.root.context).load(item.img)
                    .into(ivRunImage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            ItemRunTrackingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.onBind(position)
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