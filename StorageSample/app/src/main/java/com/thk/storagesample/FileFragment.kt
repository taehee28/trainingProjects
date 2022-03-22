package com.thk.storagesample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.thk.storagesample.databinding.FragmentFileBinding
import com.thk.storagesample.util.logd

class FileFragment : BaseFragment<FragmentFileBinding>() {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentFileBinding {
        return FragmentFileBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}
