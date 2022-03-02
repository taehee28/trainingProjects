package com.thk.layoutsample2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.thk.layoutsample2.databinding.FragmentConstraintBasicBinding

class ConstraintBasicFragment : Fragment() {
    private lateinit var binding: FragmentConstraintBasicBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConstraintBasicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btn5.setOnClickListener {
            binding.btn4.visibility = if (binding.btn4.isVisible) View.GONE else View.VISIBLE
        }
    }
}