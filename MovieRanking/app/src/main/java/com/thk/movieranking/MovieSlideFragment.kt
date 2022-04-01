package com.thk.movieranking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.thk.movieranking.adapters.ViewPagerAdapter
import com.thk.movieranking.databinding.FragmentMovieSlideBinding
import com.thk.movieranking.models.MovieListResponse
import com.thk.movieranking.network.MovieApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieSlideFragment : BaseFragment<FragmentMovieSlideBinding>() {

    private val viewPagerAdapter: ViewPagerAdapter by lazy {
        ViewPagerAdapter().apply {
            onViewClick = { id ->
                val action = MovieSlideFragmentDirections.actionMovieSlideFragmentToMovieDetailFragment(id)
                findNavController().navigate(action)
            }
        }
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMovieSlideBinding {
        return FragmentMovieSlideBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = viewPagerAdapter
        binding.indicator.setViewPager2(binding.viewPager)

        getNowPlayingMovies()
    }

    /**
     * 현재 상영 중 영화 목록 가져오기
     */
    private fun getNowPlayingMovies() = networkCoroutine {
        val response = MovieApiService.api.getNowPlayingMovies()

        viewPagerAdapter.items = response.results.slice(0..9)
        withContext(Dispatchers.Main) {
            viewPagerAdapter.notifyDataSetChanged()
        }
    }

    /**
     * 기존의 API call
     */
    private fun normalRetrofitCall() {
        val call: Call<MovieListResponse> = MovieApiService.api.getPopularMovies()

        call.enqueue(object : Callback<MovieListResponse> {
            override fun onResponse(call: Call<MovieListResponse>, response: Response<MovieListResponse>) {
                if (response.isSuccessful) {
                    val movieList = response.body()?.results
                }
            }

            override fun onFailure(call: Call<MovieListResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}