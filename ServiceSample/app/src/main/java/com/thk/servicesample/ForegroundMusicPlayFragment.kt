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

    // 서비스를 실행할 인텐트
    private val serviceIntent: Intent by lazy { Intent(requireActivity(), MusicPlayService::class.java) }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentForegroundMusicPlayBinding {
        return FragmentForegroundMusicPlayBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toggleButtonEnabled(false)

        binding.btnStartMusic.setOnClickListener { startService() }
        binding.btnStopMusic.setOnClickListener { stopService() }

    }

    /**
     * 포그라운드 서비스 시작
     */
    private fun startService() {
        // 버전에 따라 포그라운드 서비스 시작하는 방법 분기처리
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requireActivity().startForegroundService(serviceIntent)
        } else {
            requireActivity().startService(serviceIntent)
        }

        toggleButtonEnabled(true)
    }

    /**
     * 포그라운드 서비스 종료
     */
    private fun stopService() {
        requireActivity().stopService(serviceIntent)

        toggleButtonEnabled(false)
    }

    private fun toggleButtonEnabled(isPlaying: Boolean) {
        binding.btnStartMusic.isEnabled = !isPlaying
        binding.btnStopMusic.isEnabled = isPlaying
    }

}