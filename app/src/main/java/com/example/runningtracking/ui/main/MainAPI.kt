package com.example.runningtracking.ui.main

import com.example.runningtracking.model.Run
import com.example.runningtracking.ui.authen.login.model.LoginRequest
import com.example.runningtracking.ui.authen.login.model.LoginResponse
import com.example.runningtracking.ui.run.model.RunResponse
import com.example.runningtracking.ui.statistics.model.StatisticResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface MainAPI {

    @Multipart
    @POST("/api/v1/runtracking/")
    suspend fun uploadRunTracking(
        @Part img: MultipartBody.Part,
        @Part("avgSpeed") avgSpeed: RequestBody,
        @Part("distance") distance: RequestBody,
        @Part("time") time: RequestBody,
        @Part("caloriesBurned") caloriesBurned: RequestBody,
    ): Response<RunResponse>

    @GET("/api/v1/runtracking")
    suspend fun fetchRunData(): Response<List<RunResponse>>

    @POST("/api/v1/account/login/")
    suspend fun login(
        @Body login: LoginRequest
    ): Response<LoginResponse>

    @GET("api/v1/runtracking/statistic/")
    suspend fun fetchStatisticData(): Response<StatisticResponse>
}