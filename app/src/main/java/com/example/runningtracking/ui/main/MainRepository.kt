package com.example.runningtracking.ui.main

import com.example.runningtracking.model.Run
import com.example.runningtracking.ui.authen.login.model.LoginRequest
import com.example.runningtracking.ui.authen.login.model.LoginResponse
import com.example.runningtracking.ui.run.model.RunResponse
import com.example.runningtracking.ui.statistics.model.StatisticResponse
import retrofit2.Response
import javax.inject.Inject

class MainRepository @Inject constructor(private val mainConnector: MainConnector) {


    suspend fun uploadRunTracking(run: Run): Response<RunResponse> {
        return mainConnector.uploadRunTracking(run)
    }

    suspend fun fetchRunData(): Response<List<RunResponse>> {
        return mainConnector.fetchRunData()
    }

    suspend fun login(login: LoginRequest): Response<LoginResponse> {
        return mainConnector.login(login)
    }

    suspend fun fetchStatisticData(): Response<StatisticResponse> {
        return mainConnector.fetchStatisticData()
    }
}