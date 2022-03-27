package com.thk.movieranking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.thk.movieranking.adapters.ViewPagerAdapter
import com.thk.movieranking.databinding.FragmentMovieSlideBinding
import com.thk.movieranking.network.MovieApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieSlideFragment : BaseFragment<FragmentMovieSlideBinding>() {

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMovieSlideBinding {
        return FragmentMovieSlideBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        lifecycleScope.launch {
            val response = withContext(Dispatchers.IO) {
                MovieApiService.api.getNowPlayingMovies()
            }

            binding.viewPager.adapter = ViewPagerAdapter(response.results.slice(0..9)).apply {
                onViewClick = { id ->
                    val action = MovieSlideFragmentDirections.actionMovieSlideFragmentToMovieDetailFragment(id)
                    binding.root.findNavController().navigate(action)
                }
            }
            binding.indicator.setViewPager2(binding.viewPager)
        }
    }
}