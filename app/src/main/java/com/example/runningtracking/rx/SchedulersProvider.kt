package com.example.runningtracking.rx

import io.reactivex.rxjava3.core.Scheduler


interface SchedulersProvider {
    fun io(): Scheduler
    fun computation(): Scheduler
    fun ui(): Scheduler
}

