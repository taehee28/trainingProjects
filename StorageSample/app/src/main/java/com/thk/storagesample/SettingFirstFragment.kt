package com.thk.storagesample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.thk.storagesample.databinding.FragmentSettingFirstBinding
import com.thk.storagesample.util.getUserInfoPreference
import com.thk.storagesample.util.isNotEmptyOrBlank
import com.thk.storagesample.util.navigate
import com.thk.storagesample.util.setUserInfo
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

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
        // 입력된 값들 가져오기
        val rawInfo = getInputFormTextField()
        // 입력되지 않은 값이 있는지 여부
        val isNotEmpty = rawInfo.toList().all { it.isNotEmptyOrBlank() }

        try {
            check(isNotEmpty)

            // SharedPreference의 setUserInfo에 넘길 수 있는 Triple 객체 생성
            val userInfo = Triple(rawInfo.first, rawInfo.second, rawInfo.third.toInt())
            // SharedPreference에 값 쓰기 요청하고 성공 여부 받기
            val result = requireActivity().getUserInfoPreference().setUserInfo(userInfo)

            check(result ?: false)

            clearTextField()
            Toast.makeText(requireContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show()
            
        } catch (e: IllegalStateException) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "잘못된 입력입니다.", Toast.LENGTH_SHORT).show()
        }


    }

    /**
     * 3개의 TextField로부터 입력된 Text 가져오기
     */
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