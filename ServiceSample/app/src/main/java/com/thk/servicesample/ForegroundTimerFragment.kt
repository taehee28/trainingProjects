package com.thk.servicesample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.thk.servicesample.databinding.FragmentForegroundTimerBinding
import com.thk.servicesample.model.CountViewModel

class ForegroundTimerFragment : BaseFragment<FragmentForegroundTimerBinding>() {

    private val countViewModel: CountViewModel by viewModels()

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentForegroundTimerBinding {
        return DataBindingUtil.inflate(inflater, R.layout.fragment_foreground_timer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        binding.viewModel = countViewModel


    }

}