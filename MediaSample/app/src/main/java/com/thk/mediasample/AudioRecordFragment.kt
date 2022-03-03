package com.thk.mediasample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.thk.mediasample.databinding.FragmentAudioRecordBinding

class AudioRecordFragment : Fragment() {

    private lateinit var binding: FragmentAudioRecordBinding
    

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAudioRecordBinding.inflate(inflater, container, false)
        return binding.root
    }
}