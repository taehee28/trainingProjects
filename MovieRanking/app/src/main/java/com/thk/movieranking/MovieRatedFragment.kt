package com.thk.movieranking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.thk.movieranking.adapters.RatedListAdapter
import com.thk.movieranking.databinding.FragmentMovieRatedBinding
import com.thk.movieranking.network.MovieApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieRatedFragment : BaseFragment<FragmentMovieRatedBinding>() {
    private val listAdapter: RatedListAdapter by lazy {
        RatedListAdapter().apply {
            onViewClick = { id ->
                val action = MovieRatedFragmentDirections.actionMovieRatedFragmentToMovieDetailFragment(id)
                findNavController().navigate(action)
            }
        }
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMovieRatedBinding {
        return FragmentMovieRatedBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ratedList.run {
            adapter = listAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }

        getRatedList()
    }

    private fun getRatedList() = CoroutineScope(Dispatchers.IO).launch {
        requireActivity().getSessionPreference().getSessionId()?.let {
            val result = MovieApiService.api.getRatedMovies(it)
            logd(">>> $result")

            withContext(Dispatchers.Main) {
                listAdapter.submitList(result.results)
            }
        }

    }
}