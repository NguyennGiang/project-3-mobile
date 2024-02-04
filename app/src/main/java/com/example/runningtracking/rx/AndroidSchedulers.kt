package com.example.runningtracking.rx

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler

import io.reactivex.rxjava3.schedulers.Schedulers

import javax.inject.Inject


class AndroidSchedulers @Inject constructor() : SchedulersProvider {
    override fun io(): Scheduler {
        return Schedulers.io()
    }

    override fun computation(): Scheduler {
        return Schedulers.computation()
    }

    override fun ui(): Scheduler {
        return AndroidSchedulers.mainThread()
    }
}