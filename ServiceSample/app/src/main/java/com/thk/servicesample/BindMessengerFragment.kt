package com.thk.servicesample

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.thk.servicesample.databinding.FragmentBindMessengerBinding
import com.thk.servicesample.service.MessengerService

class BindMessengerFragment : BaseFragment<FragmentBindMessengerBinding>() {

    // 서비스에서 오는 메세지를 처리할 핸들러
    private val clientHandler = object : Handler(Looper.myLooper()!!) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MessengerService.MSG_SAY_HELLO -> Toast.makeText(requireContext(), "Hello from Client", Toast.LENGTH_SHORT).show()
                else -> super.handleMessage(msg)
            }
        }
    }
    // 서비스의 메신저
    private var serviceMessenger: Messenger? = null
    // 서비스한테 보낼 클라이언트의 메신저
    private val clientMessenger = Messenger(clientHandler)
    private var isBound = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
            // 서비스가 onBind에서 반환하는 IBinder를 받아 메신저의 인스턴스 생성
            serviceMessenger = Messenger(binder).apply {
                // 서비스에 바운딩되면 클라이언트의 메신저를 서비스한테 메세지로 보냄
                send(
                    Message.obtain(null, MessengerService.MSG_BIND_CLIENT, 0, 0).apply {
                        // 서비스에게 보낼 클라이언트 메신저 담기
                        replyTo = clientMessenger
                    }
                )
            }
            isBound = true

            Toast.makeText(requireContext(), "Service is bound", Toast.LENGTH_SHORT).show()
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            serviceMessenger = null
            isBound = false
        }
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentBindMessengerBinding {
        return FragmentBindMessengerBinding.inflate(inflater, container, false)
    }

    override fun onStart() {
        super.onStart()

        Intent(requireActivity(), MessengerService::class.java).also {
            requireActivity().bindService(it, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSendMessage.setOnClickListener { sendHello() }
        binding.btnReceiveMessage.setOnClickListener { receiveHello() }
    }

    /**
     * 서비스에게 토스트 띄우도록 메세지 보내기
     */
    private fun sendHello() {
        if (!isBound) return

        val msg = Message.obtain(null, MessengerService.MSG_SAY_HELLO)

        trySend {
            serviceMessenger?.send(msg)
        }
    }

    /**
     * 서비스에게 클라이언트로 토스트 띄우는 메세지를 보내라고 메세지 보내기
     */
    private fun receiveHello() {
        if (!isBound) return

        val msg = Message.obtain(null, MessengerService.MSG_SEND_TO_CLIENT)

        trySend {
            serviceMessenger?.send(msg)
        }
    }

    override fun onStop() {
        // 서비스 바운드 해제
        if (isBound) {
            // 서비스가 가지는 클라이언트의 메신저 해제처리 요청 보내기
            trySend {
                serviceMessenger?.send(
                    Message.obtain(null, MessengerService.MSG_UNBIND_CLIENT)
                )
            }
            requireActivity().unbindService(connection)
            isBound = false
        }

        super.onStop()
    }

    private inline fun trySend(block: () -> Unit) {
        try {
            block()
        } catch (e: RemoteException) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "send failed", Toast.LENGTH_SHORT).show()
        }
    }
}