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

    private var messenger: Messenger? = null
    private var isBound = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
            messenger = Messenger(binder)
            isBound = true

            Toast.makeText(requireContext(), "Service is bound", Toast.LENGTH_SHORT).show()
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            messenger = null
            isBound = false

            Toast.makeText(requireContext(), "Service is unbound", Toast.LENGTH_SHORT).show()
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

        binding.btnSendMessage.setOnClickListener {
            sendHello()
        }
    }

    private fun sendHello() {
        if (!isBound) return

        val msg = Message.obtain(null, MessengerService.MSG_SAY_HELLO, 0, 0)

        try {
            messenger?.send(msg)
        } catch (e: RemoteException) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "failed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStop() {
        if (isBound) {
            requireActivity().unbindService(connection)
            isBound = false
        }

        super.onStop()
    }
}