package com.example.runningtracking.base

import com.example.runningtracking.remote.ITokenManager
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale
import java.util.concurrent.TimeUnit

abstract class BaseServerConnector<API>(private val tokenManager: ITokenManager) {

    protected abstract fun getApiClass(): Class<API>
    private var serviceUrl = "http://192.168.1.150:8000"
    protected val api: API
        get() = initializeRetrofit().create(getApiClass())

    private fun getToken(): String = tokenManager.getToken()

    private fun initializeRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(serviceUrl)
            .client(defaultHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    private fun getHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request =
                chain.request().newBuilder()
                    .header("Accept-Language", Locale.getDefault().language)
                    .header("Cookie", "django_language=${Locale.getDefault().language}")
                    .apply {
                        try {
                            header("Authorization", getToken())
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    .build()
            chain.proceed(request)
        }
    }

    protected open val defaultHttpClient: OkHttpClient
        get() {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .addNetworkInterceptor(loggingInterceptor)
                .addInterceptor(getHeaderInterceptor())
            return client.build()
        }
}
