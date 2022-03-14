package com.thk.servicesample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.thk.servicesample.databinding.FragmentWorkerBinding

class WorkerFragment : BaseFragment<FragmentWorkerBinding>() {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentWorkerBinding {
        return FragmentWorkerBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}