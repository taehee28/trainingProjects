package com.thk.layoutsample2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.thk.layoutsample2.databinding.FragmentConstraintRatioBinding

class ConstraintRatioFragment : Fragment() {
    private lateinit var binding: FragmentConstraintRatioBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConstraintRatioBinding.inflate(inflater, container, false)

        return binding.root
    }
}