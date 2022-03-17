package com.thk.storagesample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.thk.storagesample.databinding.FragmentSettingFirstBinding

class SettingFirstFragment : BaseFragment<FragmentSettingFirstBinding>() {

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingFirstBinding {
        return FragmentSettingFirstBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.switchShared.apply {
            isChecked = requireActivity().getSharedPreferencePrivate().getSwitchValue()
            setOnCheckedChangeListener { buttonView, isChecked ->
                requireActivity().getSharedPreferencePrivate().setSwitchValue(isChecked)
            }
        }
    }

}