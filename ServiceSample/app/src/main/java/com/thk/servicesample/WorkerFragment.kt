package com.thk.servicesample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.work.WorkInfo
import com.thk.servicesample.databinding.FragmentWorkerBinding
import com.thk.servicesample.model.WorkViewModel
import java.util.*

class WorkerFragment : BaseFragment<FragmentWorkerBinding>() {

    private val viewModel: WorkViewModel by viewModels {
        WorkViewModel.WorkViewModelFactory(requireActivity().application)
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentWorkerBinding {
        return FragmentWorkerBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGetRandom.setOnClickListener {
            viewModel.generateRandomNumber()
        }
    }
}