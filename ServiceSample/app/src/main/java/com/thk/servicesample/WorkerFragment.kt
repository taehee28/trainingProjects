package com.thk.servicesample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.thk.servicesample.databinding.FragmentWorkerBinding
import com.thk.servicesample.model.WorkViewModel
import com.thk.servicesample.util.KEY_RESULT

class WorkerFragment : BaseFragment<FragmentWorkerBinding>() {

    private val viewModel: WorkViewModel by viewModels {
        WorkViewModel.WorkViewModelFactory(requireActivity().application)
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentWorkerBinding {
        return FragmentWorkerBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGetRandom.setOnClickListener { viewModel.generateRandomNumber() }
        binding.btnRandomAndSum.setOnClickListener { viewModel.generateAndSum() }
        binding.btnCoroutineWork.setOnClickListener { viewModel.coroutineWork() }
        binding.btnPeriodicWork.setOnClickListener { viewModel.periodicWork() }
        binding.btnParallelWork.setOnClickListener { viewModel.parallelWork() }
        binding.btnCombineWork.setOnClickListener { viewModel.combineWork() }
        binding.btnCancel.setOnClickListener { viewModel.cancelWork() }

        // 작업 상태 관찰
        viewModel.workInfos.observe(viewLifecycleOwner) {
//            logd(it.size.toString())
            if (it.isNullOrEmpty()) {
                return@observe
            }

            // 현재의 작업만 가져옴
            val workInfo = it[0]

            if (workInfo.state.isFinished) {
                binding.progressBar.visibility = View.INVISIBLE

                val result = workInfo.outputData.getInt(KEY_RESULT, -1)

                binding.tvResult.text = result.toString()
            } else {
                binding.progressBar.visibility = View.VISIBLE
            }

            viewModel.pruneWork()
        }

    }


}