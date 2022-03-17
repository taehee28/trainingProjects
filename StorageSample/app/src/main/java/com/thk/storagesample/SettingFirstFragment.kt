package com.thk.storagesample

import android.view.LayoutInflater
import android.view.ViewGroup
import com.thk.storagesample.databinding.FragmentSettingFirstBinding

class SettingFirstFragment : BaseFragment<FragmentSettingFirstBinding>() {

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingFirstBinding {
        return FragmentSettingFirstBinding.inflate(inflater, container, false)
    }

    
}