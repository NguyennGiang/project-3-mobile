package com.example.runningtracking.di.module

import android.app.Application
import android.content.Context
import com.example.runningtracking.remote.ITokenManager
import com.example.runningtracking.remote.TokenManager
import com.example.runningtracking.utils.SharedPreferencesManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @JvmStatic
    @Provides
    @Singleton
    internal fun provideContext(application: Application): Context {
        return application
    }

    @Provides
    @Singleton
    internal fun provideTokenManger(preferencesManager: SharedPreferencesManager,
                                    loggingInterceptor: HttpLoggingInterceptor): ITokenManager {
        return TokenManager(preferencesManager, loggingInterceptor)
    }

    @Provides
    @Singleton
    internal fun provideHttpLoggingInterceptor() = HttpLoggingInterceptor()
}