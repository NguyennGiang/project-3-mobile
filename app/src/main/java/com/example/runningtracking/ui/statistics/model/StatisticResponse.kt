package com.example.runningtracking.ui.statistics.model

import com.google.gson.annotations.SerializedName

data class StatisticResponse(
    @SerializedName("total_time")
    val totalTime: Long?,
    @SerializedName("total_distance")
    val totalDistance: Int?,
    @SerializedName("total_calories")
    val caloriesBurned: Float?,
    @SerializedName("average_speed")
    val averageSpeed: Float?
)
