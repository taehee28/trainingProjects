package com.thk.servicesample

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.thk.servicesample.databinding.FragmentForegroundTimerBinding
import com.thk.servicesample.model.CountViewModel
import com.thk.servicesample.service.TimerService

class ForegroundTimerFragment : BaseFragment<FragmentForegroundTimerBinding>() {

    private val countViewModel: CountViewModel by viewModels()
    private val serviceIntent: Intent by lazy { Intent(requireActivity(), TimerService::class.java) }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentForegroundTimerBinding {
        return DataBindingUtil.inflate(inflater, R.layout.fragment_foreground_timer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        binding.viewModel = countViewModel

        binding.btnStartTimer.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                requireActivity().startForegroundService(serviceIntent)
            } else {
                requireActivity().startService(serviceIntent)
            }
        }

        binding.btnStopTimer.setOnClickListener {
            requireActivity().stopService(serviceIntent)
        }

    }

}