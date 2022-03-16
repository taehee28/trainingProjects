package com.thk.servicesample.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.*
import android.widget.Toast

class MessengerService : Service() {

    companion object {
        const val MSG_SAY_HELLO = 1
    }

    private lateinit var messenger: Messenger

    /**
     * 메세지를 처리할 핸들러 구현
     */
    internal class IncomingHandler(
        context: Context,
        private val applicationContext: Context = context.applicationContext
    ): Handler(Looper.myLooper()!!) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_SAY_HELLO -> Toast.makeText(applicationContext, "Hello!", Toast.LENGTH_SHORT).show()
                else -> super.handleMessage(msg)
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        messenger = Messenger(IncomingHandler(this))
        return messenger.binder
    }
}