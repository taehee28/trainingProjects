package com.thk.movieranking

import android.view.LayoutInflater
import android.view.ViewGroup
import com.thk.movieranking.databinding.FragmentMovieRatedBinding

class MovieRatedFragment : BaseFragment<FragmentMovieRatedBinding>() {
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMovieRatedBinding {
        return FragmentMovieRatedBinding.inflate(inflater, container, false)
    }
}