package com.thk.servicesample.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.thk.servicesample.util.logd

class CountingService : Service() {

    private val binder = LocalBinder()

    private var number = 0

    inner class LocalBinder : Binder() {
        fun getService(): CountingService = this@CountingService
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        logd("Service Created!")
    }

    fun getNumber() = number++

}