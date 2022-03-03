package com.thk.layoutsample2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.thk.layoutsample2.databinding.FragmentHomeBinding

class HomeFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnConstraint.setOnClickListener(this)
        binding.btnLinear.setOnClickListener(this)
        binding.btnFrame.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        val action = when (view?.id) {
            binding.btnConstraint.id -> R.id.action_homeFragment_to_constraintMainFragment
            binding.btnLinear.id -> R.id.action_homeFragment_to_linearFragment
            binding.btnFrame.id -> R.id.action_homeFragment_to_frameFragment
            else -> null
        }

        if (action != null) view?.findNavController()?.navigate(action)
    }
}