package com.thk.servicesample.service

import android.app.*
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.thk.servicesample.MainActivity
import com.thk.servicesample.R

class MusicPlayService : Service() {
    companion object {
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "music_play_noti_channel"
    }

    private val TAG = MusicPlayService::class.simpleName

    private var musicPlayer: MediaPlayer? = null

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: ")

        initMusicPlayer()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
        val noti = buildNotification()
        startForeground(NOTIFICATION_ID, noti)

        return START_STICKY
    }

    private fun initMusicPlayer() {
        musicPlayer = MediaPlayer().apply {
            setDataSource(applicationContext.resources.openRawResourceFd(R.raw.vivaldi_the_four_seasons))
            setOnPreparedListener { musicPlayer?.start() }
            setOnCompletionListener { releaseMusicPlayer() }
//            setWakeMode(applicationContext, PowerManager.PARTIAL_WAKE_LOCK)

            prepareAsync()
        }
    }

    private fun releaseMusicPlayer() {
        musicPlayer = musicPlayer?.run {
            stop()
            release()
            null
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val notiChannel = NotificationChannel(CHANNEL_ID, "Music Play Channel", NotificationManager.IMPORTANCE_DEFAULT)

        val notiManager = getSystemService(NotificationManager::class.java)
        notiManager.createNotificationChannel(notiChannel)
    }

    private fun buildNotification(): Notification {
        val notiIntent = Intent(applicationContext, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notiIntent, PendingIntent.FLAG_CANCEL_CURRENT)

        return NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle("Music Play Service")
            .setContentText("Music is playing")
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_baseline_tag_faces_24)
            .setContentIntent(pendingIntent)
            .build()
    }

    override fun onDestroy() {
        releaseMusicPlayer()
        super.onDestroy()
    }


}