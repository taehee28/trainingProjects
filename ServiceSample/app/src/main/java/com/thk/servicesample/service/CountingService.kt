package com.thk.servicesample.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.thk.servicesample.util.logd

class CountingService : Service() {

    // 바인더 인스턴스
    private val binder = LocalBinder()

    private var number = 0

    // 서비스의 인스턴스를 반환하는 바인더 클래스 구현
    inner class LocalBinder : Binder() {
        fun getService(): CountingService = this@CountingService
    }

    override fun onBind(intent: Intent?): IBinder {
        // 바인더 인스턴스를 리턴하여 클라이언트에게 IBinder 제공
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        logd("Service Created!")
    }

    // 클라이언트에서 호출할 수 있는 공개 메서드
    fun getNumber() = number++

}