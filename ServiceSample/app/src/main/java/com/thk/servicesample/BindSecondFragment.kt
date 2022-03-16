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
import com.thk.servicesample.databinding.FragmentBindSecondBinding
import com.thk.servicesample.service.CountingService
import com.thk.servicesample.util.logd
import java.lang.IllegalArgumentException

class BindSecondFragment : BaseFragment<FragmentBindSecondBinding>() {
    // 서비스의 인스턴스
    private lateinit var countingService: CountingService
    private var isBound = false

    // 서비스 바인딩 상태에 대한 콜백
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
            logd("service connected")

            // 바인더를 통해 서비스의 인스턴스를 획득
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
    ): FragmentBindSecondBinding {
        return FragmentBindSecondBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBindService.setOnClickListener { bindCountingService() }
        binding.btnUnbindService.setOnClickListener { unbindCountingService() }

        binding.btnGetFromService.setOnClickListener { getNumberFromService() }
    }

    override fun onDestroy() {
        logd("onDestroy: SecondFragment")
        unbindCountingService()

        super.onDestroy()
    }

    /**
     * 서비스에 바인딩
     */
    private fun bindCountingService() {
        runIfUnbound {
            Intent(requireContext(), CountingService::class.java).also {
                requireActivity().bindService(it, connection, Context.BIND_AUTO_CREATE)
            }
        }

    }

    /**
     * 서비스와 바인딩 해제
     */
    private fun unbindCountingService() {
        runIfBound {
            requireActivity().unbindService(connection)
            isBound = false
        }
    }

    /**
     * 서비스의 공개 메서드 호출
     */
    private fun getNumberFromService() {
        runIfBound {
            val number = countingService.getNumber()
            toasting("number: $number")
        }
    }

    private inline fun runIfUnbound(block: () -> Unit) {
        try {
            require(!isBound) { "서비스가 이미 연결됨" }
            block()
        } catch (e: IllegalArgumentException) {
            toasting(e.message!!)
        }
    }

    private inline fun runIfBound(block: () -> Unit) {
        try {
            require(isBound) { "연결된 서비스가 없음" }
            block()
        } catch (e: IllegalArgumentException) {
            toasting(e.message!!)
        }
    }

    private fun toasting(msg: String) = Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
}