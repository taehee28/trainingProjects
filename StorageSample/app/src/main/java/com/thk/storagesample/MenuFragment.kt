package com.thk.storagesample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.navigation.findNavController
import com.thk.storagesample.databinding.FragmentMenuBinding
import com.thk.storagesample.util.navigate

class MenuFragment : BaseFragment<FragmentMenuBinding>() {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentMenuBinding {
        return FragmentMenuBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            btnPreference.setOnClickListener { it.navigate(R.id.action_menuFragment_to_settingFirstFragment) }
            btnLogging.setOnClickListener { it.navigate(R.id.action_menuFragment_to_loggingFragment) }
            btnFile.setOnClickListener { it.navigate(R.id.action_menuFragment_to_fileFragment) }
        }
    }


}