package com.example.runningtracking.ui.run.model

import com.google.gson.annotations.SerializedName

data class RunResponse(
    var img: String? = null,
    var avgSpeed: Float = 0f,
    var distance: Int = 0,
    var time: Long = 0L,
    var caloriesBurned: Int = 0,
    var attachmentPath: String = "",
    @SerializedName("created_at")
    var createdAt: String = ""
)
