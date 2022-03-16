package com.thk.servicesample.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.*
import android.widget.Toast
import com.thk.servicesample.util.logd

class MessengerService : Service() {

    companion object {
        const val MSG_SAY_HELLO = 1
        const val MSG_SEND_TO_CLIENT = 2
        const val MSG_BIND_CLIENT = 3
        const val MSG_UNBIND_CLIENT = 4
    }

    // 서비스의 메신저 
    private lateinit var serviceMessenger: Messenger

    /**
     * 메세지를 처리할 핸들러 구현
     */
    internal class IncomingHandler(
        context: Context,
        private val applicationContext: Context = context.applicationContext
    ): Handler(Looper.myLooper()!!) {

        // 클라이언트의 메신저
        private var clientMessenger: Messenger? = null

        override fun handleMessage(msg: Message) {
            when (msg.what) {
                // 클라이언트가 보내준 클라이언트의 메신저 획득
                MSG_BIND_CLIENT -> {
                    clientMessenger = msg.replyTo
                    logd("connect")
                }

                // 서비스 unbind 할 때 메신저도 null 처리
                MSG_UNBIND_CLIENT -> {
                    clientMessenger = null
                    logd("disconnect")
                }

                // 클라이언트에게 메신저로 메세지 보내기
                MSG_SEND_TO_CLIENT -> {
                    val msg = Message.obtain(null, MSG_SAY_HELLO)

                    try {
                        clientMessenger?.send(msg)
                    } catch (e: RemoteException) {
                        e.printStackTrace()
                    }
                }

                // 토스트 띄우기
                MSG_SAY_HELLO -> Toast.makeText(applicationContext, "Hello from Service", Toast.LENGTH_SHORT).show()
                else -> super.handleMessage(msg)
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        // 메신저 인스턴스 생성하기
        serviceMessenger = Messenger(IncomingHandler(this))
        // 메신저가 생성하는 IBinder 리턴
        return serviceMessenger.binder
    }
}