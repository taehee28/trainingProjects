package com.thk.servicesample.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.thk.servicesample.R

/**
 * 상단 알림 띄우는 작업을 하는 Worker
 */
class NotificationWorker(context: Context, params: WorkerParameters) :  Worker(context, params) {
    override fun doWork(): Result {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("EXAMPLE_NOTIFICATION", "Example Notification", NotificationManager.IMPORTANCE_HIGH)

            val notiManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
            notiManager?.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(applicationContext, "EXAMPLE_NOTIFICATION")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Notification from Worker")
            .setContentText("^_^")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(LongArray(0))

        NotificationManagerCompat.from(applicationContext).notify(1, builder.build())

        return Result.success()
    }
}