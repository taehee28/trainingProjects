package com.thk.mediasample

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.thk.mediasample.databinding.FragmentMusicPlayBinding
import kotlin.properties.Delegates

class MusicPlayFragment : Fragment() {
    private val TAG = MusicPlayFragment::class.simpleName

    private var _binding: FragmentMusicPlayBinding? = null
    private val binding
        get() = _binding!!

    private val btnStateModel: BtnStateViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_music_play, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        binding.viewModel = btnStateModel

    }

}