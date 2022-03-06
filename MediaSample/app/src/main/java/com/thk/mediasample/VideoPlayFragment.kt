package com.thk.mediasample

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.thk.mediasample.databinding.FragmentVideoPlayBinding


class VideoPlayFragment : Fragment() {

    private var _binding: FragmentVideoPlayBinding? = null
    private val binding
        get() = _binding!!

    private var videoPlayer: ExoPlayer? = null
    private val videoUri: Uri by lazy {
        Uri.parse("android.resource://${requireActivity().packageName}/${R.raw.bird}")
    }

    private val playWhenReady = false
    private var playbackPosition = 0L


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVideoPlayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        initializePlayer()
    }

    private fun initializePlayer() {
        videoPlayer = ExoPlayer.Builder(requireContext())
            .build()
            .also { exoPlayer ->
                binding.playerView.player = exoPlayer

                val mediaItem = MediaItem.fromUri(videoUri)
                exoPlayer.setMediaItem(mediaItem)
                exoPlayer.seekTo(playbackPosition)
                exoPlayer.playWhenReady = playWhenReady
                exoPlayer.prepare()
            }


    }

    override fun onStop() {
        releasePlayer()

        super.onStop()
    }

    private fun releasePlayer() {
        binding.playerView.player = null

        videoPlayer = videoPlayer?.run {
            playbackPosition = this.currentPosition
            release()
            null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}