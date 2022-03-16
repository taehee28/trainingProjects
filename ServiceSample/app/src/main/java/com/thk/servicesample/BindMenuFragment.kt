package com.thk.servicesample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.thk.servicesample.databinding.FragmentBindMenuBinding

class BindMenuFragment : BaseFragment<FragmentBindMenuBinding>() {

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentBindMenuBinding {
        return FragmentBindMenuBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBinder.setOnClickListener {
            it.findNavController().navigate(R.id.action_bindMenuFragment_to_bindFirstFragment)
        }
        binding.btnMessenger.setOnClickListener {
            it.findNavController().navigate(R.id.action_bindMenuFragment_to_bindMessengerFragment)
        }
    }
}