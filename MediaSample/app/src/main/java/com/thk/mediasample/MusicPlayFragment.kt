package com.thk.mediasample

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.thk.mediasample.databinding.FragmentMusicPlayBinding

class MusicPlayFragment : Fragment() {

    private var _binding: FragmentMusicPlayBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMusicPlayBinding.inflate(inflater, container, false)

        return binding.root
    }



}