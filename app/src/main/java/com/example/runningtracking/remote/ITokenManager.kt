package com.example.runningtracking.remote

interface ITokenManager {
    fun getToken(): String
    suspend fun refreshToken()
}