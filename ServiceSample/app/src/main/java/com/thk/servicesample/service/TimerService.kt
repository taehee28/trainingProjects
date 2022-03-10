package com.thk.servicesample.service

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.thk.servicesample.MainActivity
import com.thk.servicesample.R
import com.thk.servicesample.model.Actions

class TimerService : Service() {
    companion object {
        const val NOTIFICATION_ID = 100
        const val CHANNEL_ID = "timer_noti_channel"
    }

    private val TAG = TimerService::class.simpleName

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: ")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }

        val noti = buildNotification()
        startForeground(NOTIFICATION_ID, noti)

        return START_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val notiChannel = NotificationChannel(CHANNEL_ID, "Timer Channel", NotificationManager.IMPORTANCE_DEFAULT)

        val notiManager = getSystemService(NotificationManager::class.java)
        notiManager.createNotificationChannel(notiChannel)
    }

    private fun buildNotification(): Notification {
        val notiIntent = Intent(applicationContext, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notiIntent, PendingIntent.FLAG_CANCEL_CURRENT)

        return NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle("Timer Service")
            .setContentText("timer is running!")
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .build()
    }


}