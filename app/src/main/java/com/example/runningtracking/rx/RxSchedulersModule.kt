package com.example.runningtracking.rx

import com.example.runningtracking.ui.main.MainConnector
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class RxSchedulersModule {
    @Binds
    abstract fun bindSchedulersProvider(schedulers: AndroidSchedulers): SchedulersProvider
}

