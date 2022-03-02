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
        binding.btnDimension.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        val action = when(view?.id) {
            binding.btnBasic.id -> R.id.action_constraintMainFragment_to_constraintBasicFragment
            binding.btnDimension.id -> R.id.action_constraintMainFragment_to_constraintDimensionFragment
            else -> null
        }

        if (action != null) view?.findNavController()?.navigate(action)
    }
}