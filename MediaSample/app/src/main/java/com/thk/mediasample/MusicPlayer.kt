package com.thk.mediasample

import android.content.Context
import android.media.MediaPlayer
import android.util.Log

class MusicPlayer private constructor(
    private var mediaPlayer: MediaPlayer?
){
    private val TAG = MusicPlayer::class.simpleName
    companion object {
        fun createPlayer(context: Context) = MusicPlayer(MediaPlayer.create(context, R.raw.vivaldi_the_four_seasons))
    }

    fun startMusic() {
        mediaPlayer?.start()
    }

    fun pauseMusic() {
        mediaPlayer?.pause()
    }

    fun stopMusic() {
        mediaPlayer?.stop()
    }

    fun releasePlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
    }


}