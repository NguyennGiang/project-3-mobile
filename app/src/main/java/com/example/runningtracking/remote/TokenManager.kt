package com.example.runningtracking.remote

import com.example.runningtracking.utils.SharedPreferencesManager
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Inject

class TokenManager(private val preferencesManager: SharedPreferencesManager,
                   private val loggingInterceptor: HttpLoggingInterceptor): ITokenManager {
    override fun getToken(): String {
        return preferencesManager.token
    }

    override suspend fun refreshToken() {

    }
}