package com.thk.storagesample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.thk.storagesample.databinding.FragmentSettingSecondBinding
import com.thk.storagesample.util.getUserInfo
import com.thk.storagesample.util.getUserInfoPreference

class SettingSecondFragment : BaseFragment<FragmentSettingSecondBinding>() {
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingSecondBinding {
        return FragmentSettingSecondBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLoad.setOnClickListener {
            loadUserInfo()
        }
    }

    private fun loadUserInfo() {
        // SharedPreference에서 받은 Triple 객체를 구조분해하여 받음
        val (name, email, age) = requireActivity().getUserInfoPreference().getUserInfo()

        // 화면에 표시
        binding.run {
            tfName.setText(name)
            tfEmail.setText(email)
            tfAge.setText(age.toString())
        }
    }

}