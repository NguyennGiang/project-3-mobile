package com.example.runningtracking.ui.main

import android.webkit.MimeTypeMap
import com.example.runningtracking.base.BaseServerConnector
import com.example.runningtracking.model.Run
import com.example.runningtracking.remote.ITokenManager
import com.example.runningtracking.ui.authen.login.model.LoginRequest
import com.example.runningtracking.ui.authen.login.model.LoginResponse
import com.example.runningtracking.ui.run.model.RunResponse
import com.example.runningtracking.ui.statistics.model.StatisticResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class MainConnector @Inject constructor(tokenManager: ITokenManager) : BaseServerConnector<MainAPI>(tokenManager) {
    override fun getApiClass(): Class<MainAPI> {
        return MainAPI::class.java
    }

    private fun convertFileToMultipartBody(file: File, mimeType: String): MultipartBody.Part {
        val requestBody = file.asRequestBody(mimeType.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("img", file.name, requestBody)
    }

    private fun convertToForm(description: String): RequestBody {
        return description.toRequestBody(MultipartBody.FORM)
    }

    suspend fun uploadRunTracking(run: Run): Response<RunResponse> {
        val filePath = run.attachmentPath
        val file = File(filePath)
        val extension = filePath.substring(filePath.lastIndexOf(".") + 1)
        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        return if (mimeType != null){
            val avgSpeed = convertToForm(run.avgSpeed.toString())
            val distance = convertToForm(run.distance.toString())
            val time = convertToForm(run.time.toString())
            val caloriesBurned = convertToForm(run.caloriesBurned.toString())
            val img = convertFileToMultipartBody(file, mimeType)
            api.uploadRunTracking(img, avgSpeed, distance, time, caloriesBurned)
        }
        else Response.error(400, "Fail to upload".toResponseBody())
    }

    suspend fun fetchRunData(): Response<List<RunResponse>> {
        return api.fetchRunData()
    }

    suspend fun login(login: LoginRequest): Response<LoginResponse> {
        return api.login(login)
    }

    suspend fun fetchStatisticData(): Response<StatisticResponse> {
        return api.fetchStatisticData()
    }
}