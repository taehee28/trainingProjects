package com.thk.movieranking

import android.view.LayoutInflater
import android.view.ViewGroup
import com.thk.movieranking.databinding.FragmentMoviePopularBinding

class PopularMoviesFragment : BaseFragment<FragmentMoviePopularBinding>() {

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMoviePopularBinding {
        return FragmentMoviePopularBinding.inflate(inflater, container, false)
    }
}