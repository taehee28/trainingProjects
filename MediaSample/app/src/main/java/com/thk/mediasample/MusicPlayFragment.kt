package com.thk.mediasample

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.thk.mediasample.databinding.FragmentMusicPlayBinding
import com.thk.mediasample.ControlBtnState.*


class MusicPlayFragment : Fragment() {
    private val TAG = MusicPlayFragment::class.simpleName

    private var _binding: FragmentMusicPlayBinding? = null
    private val binding
        get() = _binding!!

    private val btnStateModel: BtnStateViewModel by viewModels()
    private var mediaPlayer: MediaPlayer? = null

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

        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.vivaldi_the_four_seasons)
        mediaPlayer?.setOnPreparedListener {
            Log.d(TAG, ">>>> onPreparedListener: prepare done!")
            Log.d(TAG, "duration : ${mediaPlayer?.duration}")
            binding.seekBar.max = mediaPlayer?.duration!!
            btnStateModel.changeBtnState(READY)
        }
//        mediaPlayer = MediaPlayer().apply {
//            setAudioAttributes(
//                AudioAttributes.Builder()
//                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                    .setUsage(AudioAttributes.USAGE_MEDIA)
//                    .build()
//            )
//            setDataSource("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3")
//            setOnPreparedListener {
//                binding.seekBar.max = mediaPlayer?.duration!!
//                btnStateModel.changeBtnState(READY)
//            }
//
//        }

        btnStateModel.btnState.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "observe: state = ${it.name}")
            when(it!!) {
                NOT_READY -> mediaPlayer?.prepare()
                PLAYING -> mediaPlayer?.start()
                PAUSED -> mediaPlayer?.pause()
                STOPPED -> {
                    mediaPlayer?.stop()
                    btnStateModel.changeBtnState(NOT_READY)
                }

            }
        })

    }

    override fun onStop() {
        mediaPlayer?.release()
        mediaPlayer = null
        super.onStop()
    }

}