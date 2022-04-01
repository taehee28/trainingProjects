package com.thk.movieranking

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.thk.movieranking.databinding.DialogRatingBinding
import com.thk.movieranking.databinding.FragmentMovieDetailBinding
import com.thk.movieranking.models.Movie
import com.thk.movieranking.models.RatingValue
import com.thk.movieranking.network.MovieApiService
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

        setupToolbar()

        getMovieDetailFromServer(movieId)

        binding.btnDoRate.setOnClickListener { showRatingDialog() }
    }

    /**
     * 상단 툴바 네비게이션이랑 연결하기
     */
    private fun setupToolbar() {
        val navController = findNavController()
        val appBarConfig = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfig)
    }

    /**
     * 서버에서 영화 상세 정보 가져오기
     */
    private fun getMovieDetailFromServer(movieId: Int) {
        if (movieId == -1) {
            Toast.makeText(requireContext(), "movie id = -1", Toast.LENGTH_SHORT).show()
            return
        }

        networkCoroutine {
            // 영화 상세 정보 가져오기
            val movieDetail = MovieApiService.api.getMovieDetail(movieId)

            updateUi(movieDetail)
        }
    }

    /**
     * 메인 스레드에서 UI 업데이트
     */
    private suspend fun updateUi(movieDetail: Movie) = withContext(Dispatchers.Main) {
        GlideApp.with(binding.ivPoster)
            .load(TMDB_IMAGE_URL + movieDetail.posterPath)
            .into(binding.ivPoster)

        binding.run {
            toolbar.title = movieDetail.title
            tvGenre.text = movieDetail.genres.joinToString(separator = ", ") { it.name }
            tvReleaseDate.text = movieDetail.releaseDate + " 개봉"
            tvRuntime.text = String.format("%d분", movieDetail.runtime)
            tvOverview.text = movieDetail.overview ?: ""
        }
    }

    /**
     * 평점 매기는 다이얼로그 띄우기
     */
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

    /**
     * 매긴 평점 서버로 전송하기
     */
    private fun sendRatingToServer(ratingValue: Int) = CoroutineScope(Dispatchers.IO).launch {
        val sessionId = requireActivity().getSessionPreference().getSessionId() ?: return@launch

        // 영화에 평점 매기기 위한 POST 요청
        val result = MovieApiService.api.ratingMovie(guestSessionId = sessionId, movieId = movieId, value = RatingValue(ratingValue.toDouble()))
        logd(result.toString())
    }
}