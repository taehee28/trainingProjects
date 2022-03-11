package com.thk.servicesample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.thk.servicesample.databinding.FragmentMenuBinding

class MenuFragment : BaseFragment<FragmentMenuBinding>() {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentMenuBinding {
        return FragmentMenuBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnForeground.setOnClickListener {
            it.findNavController().navigate(R.id.action_menuFragment_to_foregroundMusicPlayFragment)
        }
        binding.btnBind.setOnClickListener {
            it.findNavController().navigate(R.id.action_menuFragment_to_bindFirstFragment)
        }
    }
}