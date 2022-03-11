package com.thk.servicesample

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.thk.servicesample.databinding.FragmentBindFirstBinding
import com.thk.servicesample.service.CountingService
import com.thk.servicesample.util.logd
import kotlin.properties.Delegates

class BindFirstFragment : BaseFragment<FragmentBindFirstBinding>() {
    private lateinit var countingService: CountingService
    private var isBound = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
            logd("service connected")

            val localBinder = binder as CountingService.LocalBinder
            countingService = localBinder.getService()

            isBound = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            logd("service disconnected")

            isBound = false
        }
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentBindFirstBinding {
        return FragmentBindFirstBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBindService.setOnClickListener { bindCountingService() }
        binding.btnUnbindService.setOnClickListener { unbindCountingService() }

        binding.btnGetFromService.setOnClickListener { getNumberFromService() }

    }

    override fun onDestroy() {
        logd("onDestroy: FirstFragment")
        unbindCountingService()
        
        super.onDestroy()
    }

    private fun bindCountingService() {
        if (!isBound) {
            Intent(requireContext(), CountingService::class.java).also {
                requireActivity().bindService(it, connection, Context.BIND_AUTO_CREATE)
            }
        } else {
            toasting("이미 연결되어 있습니다.")
        }
    }

    private fun unbindCountingService() {
        if (isBound) {
            requireActivity().unbindService(connection)
            isBound = false
        } else {
            toasting("해제할 서비스가 없습니다.")
        }
    }

    private fun getNumberFromService() {
        if (isBound) {
            val number = countingService.getNumber()
            toasting("number: $number")
        } else {
            toasting("연결된 서비스가 없습니다.")
        }
    }

    private fun toasting(msg: String) = Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
}