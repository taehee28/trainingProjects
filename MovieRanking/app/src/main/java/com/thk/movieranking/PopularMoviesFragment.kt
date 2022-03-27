package com.thk.movieranking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.thk.movieranking.adapters.ViewPagerAdapter
import com.thk.movieranking.databinding.FragmentMoviePopularBinding
import com.thk.movieranking.network.MovieApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PopularMoviesFragment : BaseFragment<FragmentMoviePopularBinding>() {

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMoviePopularBinding {
        return FragmentMoviePopularBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        lifecycleScope.launch {
            val response = withContext(Dispatchers.IO) {
                MovieApiService.api.getPopularMovies()
            }

            binding.viewPager.adapter = ViewPagerAdapter(response.results.slice(0..9))
        }
    }
}