package com.thk.movieranking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import com.thk.movieranking.adapters.SearchResultAdapter
import com.thk.movieranking.databinding.FragmentMovieSearchBinding
import com.thk.movieranking.network.MovieApiService
import kotlinx.coroutines.*

class MovieSearchFragment : BaseFragment<FragmentMovieSearchBinding>() {

    private val listAdapter: SearchResultAdapter by lazy { SearchResultAdapter() }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMovieSearchBinding {
        return FragmentMovieSearchBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchResultList.adapter = listAdapter
        binding.searchResultList.addItemDecoration(DividerItemDecoration(requireContext(), VERTICAL))

        binding.btnSearch.setOnClickListener {
            val input = binding.textField.text.toString().trim()
            if (input.isEmpty()) return@setOnClickListener

            search(input)
        }
    }

    private fun search(text: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = MovieApiService.api.searchMovie(queryText = text)
            logd(result.results.toString())

            withContext(Dispatchers.Main) {
                listAdapter.submitList(result.results)
            }
        }
    }
}