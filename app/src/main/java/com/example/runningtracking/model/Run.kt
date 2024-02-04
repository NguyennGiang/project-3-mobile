package com.example.runningtracking.model

import android.graphics.Bitmap

data class Run(
    var img: Bitmap? = null,
    var avgSpeed: Float = 0f,
    var distance: Int = 0,
    var time: Long = 0L,
    var caloriesBurned: Int = 0,
    var attachmentPath: String = ""
)