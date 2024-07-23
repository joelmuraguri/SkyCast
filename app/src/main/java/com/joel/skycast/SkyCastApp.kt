package com.joel.skycast

import android.app.Application
import com.joel.sync.worker.WorkerInitializer
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SkyCastApp : Application() {
    override fun onCreate() {
        super.onCreate()
        WorkerInitializer.initialize(this)
    }
}
