package com.thk.movieranking

import android.view.LayoutInflater
import android.view.ViewGroup
import com.thk.movieranking.databinding.FragmentMovieSearchBinding

class MovieSearchFragment : BaseFragment<FragmentMovieSearchBinding>() {

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMovieSearchBinding {
        return FragmentMovieSearchBinding.inflate(inflater, container, false)
    }
}