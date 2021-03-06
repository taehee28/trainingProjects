package com.thk.layoutsample2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.thk.layoutsample2.databinding.FragmentConstraintMainBinding

class ConstraintMainFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentConstraintMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConstraintMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBasic.setOnClickListener(this)
        binding.btnRatio.setOnClickListener(this)
        binding.btnChain.setOnClickListener(this)
        binding.btnFlow.setOnClickListener(this)
        binding.btnGuideline.setOnClickListener(this)
        binding.btnBarrier.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        val action = when(view?.id) {
            binding.btnBasic.id -> R.id.action_constraintMainFragment_to_constraintBasicFragment
            binding.btnRatio.id -> R.id.action_constraintMainFragment_to_constraintRatioFragment
            binding.btnChain.id -> R.id.action_constraintMainFragment_to_constraintChainFragment
            binding.btnFlow.id -> R.id.action_constraintMainFragment_to_constraintFlowFragment
            binding.btnGuideline.id -> R.id.action_constraintMainFragment_to_constraintGuidelineFragment
            binding.btnBarrier.id -> R.id.action_constraintMainFragment_to_constraintBarrierFragment
            else -> null
        }

        if (action != null) view?.findNavController()?.navigate(action)
    }
}