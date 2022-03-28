package com.thk.movieranking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.thk.movieranking.databinding.FragmentMovieDetailBinding
import com.thk.movieranking.network.MovieApiService
import com.thk.movieranking.utils.GlideApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieDetailFragment : BaseFragment<FragmentMovieDetailBinding>() {

    private val args: MovieDetailFragmentArgs by navArgs()

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMovieDetailBinding {
        return FragmentMovieDetailBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movieId = args.movieId

        lifecycleScope.launch(Dispatchers.IO) {
            val movieDetail = MovieApiService.api.getMovieDetail(movieId)
            logd(movieDetail.toString())

            val videoId = if (movieDetail.hasVideo) {
                MovieApiService.api.getMovieVideoInfo(movieId).key
            } else {
                null
            }

            withContext(Dispatchers.Main) {
                GlideApp.with(binding.ivPoster)
                    .load(TMDB_IMAGE_URL + movieDetail.posterPath)
                    .into(binding.ivPoster)

                binding.run {
                    toolbar.title = movieDetail.title
                    tvGenre.text = movieDetail.genres.joinToString(separator = ", ") { it.name }
                    tvReleaseDate.text = movieDetail.releaseDate + " 개봉"
                    tvRuntime.text = String.format("%d분", movieDetail.runtime)
                    tvOverview.text = movieDetail.overview ?: ""

                    videoId?.let {
                        youtubePlayer.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
                            override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                                youTubePlayer.loadVideo(it, 0f)
                            }
                        })
                    } ?: kotlin.run {
                        youtubePlayer.visibility = View.GONE
                    }
                }
            }
        }
    }
}