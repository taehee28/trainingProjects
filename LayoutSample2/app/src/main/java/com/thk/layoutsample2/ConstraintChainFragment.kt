package com.thk.layoutsample2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.thk.layoutsample2.databinding.FragmentConstraintChainBinding

class ConstraintChainFragment : Fragment() {
    private lateinit var binding: FragmentConstraintChainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConstraintChainBinding.inflate(inflater, container, false)
        return binding.root
    }
}