package com.thk.mediasample

import android.media.MediaPlayer
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.thk.mediasample.databinding.FragmentMusicPlayBinding
import com.thk.mediasample.data.ControlBtnState.*
import com.thk.mediasample.data.ControlBtnStateViewModel


class MusicPlayFragment : Fragment() {
    private val TAG = MusicPlayFragment::class.simpleName

    private var _binding: FragmentMusicPlayBinding? = null
    private val binding
        get() = _binding!!

    private val btnStateModel: ControlBtnStateViewModel by viewModels()
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

        btnStateModel.btnState.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "observe: state = ${it.name}")
            when(it!!) {
                LOADING -> loadingMusic()
                PLAYING -> playMusic()
                PAUSED -> mediaPlayer?.pause()
                STOPPED -> {
                    mediaPlayer?.stop()
                    binding.seekBar.progress = 0
                    btnStateModel.changeBtnState(LOADING)
                }
            }
        })

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer?.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) { }

            override fun onStopTrackingTouch(p0: SeekBar?) { }
        })
    }

    private fun loadingMusic() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(requireContext().resources.openRawResourceFd(R.raw.vivaldi_the_four_seasons))
                setOnPreparedListener {
                    btnStateModel.changeBtnState(READY)
                    binding.seekBar.max = it.duration
                }
                setOnCompletionListener {
                    btnStateModel.changeBtnState(STOPPED)
                }
            }
        }
        mediaPlayer?.prepare()
    }

    private fun playMusic() {
        mediaPlayer?.start()

        // SeekBar 움직이기 위한 스레드
        object : Thread() {
            override fun run() {
                super.run()

                while (mediaPlayer?.isPlaying!!) {
                    activity?.runOnUiThread {
                        binding.seekBar.progress = mediaPlayer?.currentPosition!!
                    }
                    SystemClock.sleep(200)
                }
            }
        }.start()
    }

    override fun onStop() {
        mediaPlayer?.release()
        mediaPlayer = null
        super.onStop()
    }

}