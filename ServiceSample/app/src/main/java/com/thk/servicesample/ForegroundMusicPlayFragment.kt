package com.thk.servicesample

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.thk.servicesample.databinding.FragmentForegroundMusicPlayBinding
import com.thk.servicesample.service.MusicPlayService

class ForegroundMusicPlayFragment : BaseFragment<FragmentForegroundMusicPlayBinding>() {

    private val serviceIntent: Intent by lazy { Intent(requireActivity(), MusicPlayService::class.java) }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentForegroundMusicPlayBinding {
        return FragmentForegroundMusicPlayBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnStartTimer.setOnClickListener {
            startService()
        }

        binding.btnStopTimer.setOnClickListener {
            stopService()
        }

    }

    private fun startService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requireActivity().startForegroundService(serviceIntent)
        } else {
            requireActivity().startService(serviceIntent)
        }

        toggleButtonEnabled(true)
    }

    private fun stopService() {
        requireActivity().stopService(serviceIntent)

        toggleButtonEnabled(false)
    }

    private fun toggleButtonEnabled(isPlaying: Boolean) {
        binding.btnStartTimer.isEnabled = !isPlaying
        binding.btnStopTimer.isEnabled = isPlaying
    }

}