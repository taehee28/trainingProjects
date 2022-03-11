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
import androidx.navigation.findNavController
import com.thk.servicesample.databinding.FragmentBindFirstBinding
import com.thk.servicesample.service.CountingService
import com.thk.servicesample.util.logd

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

        binding.btnMoveToSecond.setOnClickListener {
            it.findNavController().navigate(R.id.action_bindFirstFragment_to_bindSecondFragment)
        }

    }

    override fun onDestroy() {
        logd("onDestroy: FirstFragment")
        unbindCountingService()

        super.onDestroy()
    }

    private fun bindCountingService() {
        check(checkUnbound) {
            Intent(requireContext(), CountingService::class.java).also {
                requireActivity().bindService(it, connection, Context.BIND_AUTO_CREATE)
            }
        }
    }

    private fun unbindCountingService() {
        check(checkBound) {
            requireActivity().unbindService(connection)
            isBound = false
        }
    }

    private fun getNumberFromService() {
        check(checkBound) {
            val number = countingService.getNumber()
            toasting("number: $number")
        }
    }

    private inline fun check(checkBlock: () -> Unit, block: () -> Unit) {
        try {
            checkBlock()
            block()
        } catch (e: Exception) {
            toasting(e.message!!)
        }
    }

    private val checkUnbound = {
        if (isBound) {
            throw Exception("이미 연결되어 있습니다.")
        }
    }

    private val checkBound = {
        if (!isBound) {
            throw Exception("연결된 서비스가 없습니다.")
        }
    }

    private fun toasting(msg: String) = Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
}