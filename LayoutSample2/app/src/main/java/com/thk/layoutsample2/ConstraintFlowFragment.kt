package com.thk.layoutsample2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.thk.layoutsample2.databinding.FragmentConstraintFlowBinding

class ConstraintFlowFragment : Fragment() {
    private lateinit var binding: FragmentConstraintFlowBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConstraintFlowBinding.inflate(inflater, container, false)
        return binding.root
    }
}