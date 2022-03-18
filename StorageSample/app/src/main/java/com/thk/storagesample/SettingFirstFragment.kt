package com.thk.storagesample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.thk.storagesample.databinding.FragmentSettingFirstBinding
import com.thk.storagesample.util.getUserInfoPreference
import com.thk.storagesample.util.setUserInfo
import java.lang.IllegalArgumentException

class SettingFirstFragment : BaseFragment<FragmentSettingFirstBinding>() {

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingFirstBinding {
        return FragmentSettingFirstBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSave.setOnClickListener {
           saveUserInfo()
        }

        binding.btnMoveToSecond.setOnClickListener { it.navigate(R.id.action_settingFirstFragment_to_settingSecondFragment) }
    }

    private fun saveUserInfo() {
        val rawInfo = getInputFormTextField()
        val isNotEmpty = rawInfo.toList().all { it.isNotEmptyOrBlank() }

        try {
            require(isNotEmpty)

            val userInfo = Triple(rawInfo.first, rawInfo.second, rawInfo.third.toInt())
            val result = requireActivity().getUserInfoPreference().setUserInfo(userInfo)

            require(result ?: false)

            clearTextField()
            Toast.makeText(requireContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show()
            
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "잘못된 입력입니다.", Toast.LENGTH_SHORT).show()
        }


    }

    private fun getInputFormTextField(): Triple<String, String, String> {
        val name = binding.tfName.text.toString()
        val email = binding.tfEmail.text.toString()
        val strAge = binding.tfAge.text.toString()

        return Triple(name, email, strAge)
    }

    private fun clearTextField() {
        binding.run {
            tfName.setText("")
            tfEmail.setText("")
            tfAge.setText("")
        }
    }
}