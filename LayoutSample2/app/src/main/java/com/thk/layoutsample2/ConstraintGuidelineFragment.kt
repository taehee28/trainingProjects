package com.thk.layoutsample2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.thk.layoutsample2.databinding.FragmentConstraintGuidelineBinding

class ConstraintGuidelineFragment : Fragment() {
    private lateinit var binding: FragmentConstraintGuidelineBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConstraintGuidelineBinding.inflate(inflater, container, false)
        return binding.root
    }
}