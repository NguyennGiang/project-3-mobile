package com.example.runningtracking.ui.authen.login.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("token")
    val token: String?,
    @SerializedName("refresh_token")
    val refreshToken: String?,
    @SerializedName("user")
    val userID: String?
)
