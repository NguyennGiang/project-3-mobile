package com.example.runningtracking.ui.tracking


data class RunningTrackingResponse(
    val id: String?,
    val img: String?,
    val avgSpeed: Float?,
    val distance: Int?,
    val time: Int?,
    val caloriesBurned: Float?
)