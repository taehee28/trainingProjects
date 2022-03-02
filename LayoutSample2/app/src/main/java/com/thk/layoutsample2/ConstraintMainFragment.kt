package com.thk.layoutsample2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.thk.layoutsample2.databinding.FragmentConstraintMainBinding

class ConstraintMainFragment : Fragment() {
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

        binding.btnBasic.setOnClickListener {
            it.findNavController().navigate(R.id.action_constraintMainFragment_to_constraintBasicFragment)
        }
    }
}