package com.thk.movieranking

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.thk.movieranking.databinding.DialogRatingBinding
import com.thk.movieranking.databinding.FragmentMovieDetailBinding
import com.thk.movieranking.models.RatingValue
import com.thk.movieranking.network.MovieApiService
import com.thk.movieranking.network.getSessionId
import com.thk.movieranking.utils.GlideApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.min

class MovieDetailFragment : BaseFragment<FragmentMovieDetailBinding>() {

    private val args: MovieDetailFragmentArgs by navArgs()
    private val movieId: Int by lazy { args.movieId }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMovieDetailBinding {
        return FragmentMovieDetailBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getMovieDetailFromServer(movieId)

        binding.btnDoRate.setOnClickListener { showRatingDialog() }
    }

    private fun getMovieDetailFromServer(movieId: Int) {
        // TODO: 메소드 쪼개고 정리하기

        if (movieId == -1) return

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

    private fun showRatingDialog() {
        val dialogBinding = DialogRatingBinding.inflate(LayoutInflater.from(requireContext()), binding.root, false).apply {
            numberPicker.apply {
                minValue = 0
                maxValue = 10
            }
        }

        AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .setNegativeButton("취소") { dialogInterface, n -> }
            .setPositiveButton("확인") { dialogInterface, n ->
                sendRatingToServer(dialogBinding.numberPicker.value)
            }
            .show()
    }

    private fun sendRatingToServer(ratingValue: Int) = CoroutineScope(Dispatchers.Main).launch {
        // TODO: 세션 아이디 바뀌는거 해결하기  
        val sessionId = getSessionId()
        logd(sessionId ?: "null")
        check(!sessionId.isNullOrEmpty()) { "sessionId가 없음" }

        val result = MovieApiService.api.ratingMovie(guestSessionId = sessionId, movieId = movieId, value = RatingValue(ratingValue.toDouble()))
        logd(result.toString())
    }
}