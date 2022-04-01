package com.thk.movieranking

import android.app.AlertDialog
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
            onViewLongClick = { id ->
                showDeleteDialog(id)
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

    /**
     * 평점 삭제 다이얼로그 띄우기
     */
    private fun showDeleteDialog(movieId: Int) {
        AlertDialog.Builder(requireContext())
            .setItems(arrayOf("평가 삭제하기")) { dialogInterface, posistion ->
                deleteRating(movieId)
            }
            .show()
    }

    /**
     * 서버로 평점 삭제 요청하기
     */
    private fun deleteRating(movieId: Int) = networkCoroutine {
        requireActivity().getSessionPreference().getSessionId()?.let {
            // 영화에 매긴 평점 지우기 위한 DELETE 요청
            val result = MovieApiService.api.deleteRating(movieId = movieId, guestSessionId = it)

            if (result.statusCode == 13) {
                getRatedList()
            } else {
                logd(result.toString())
            }
        }
    }

    /**
     * 평점 매긴 영화 목록 가져오기
     */
    private fun getRatedList() = networkCoroutine {
        // 기기에 세션 아이디 저장되어있으면 있으면 실행
        requireActivity().getSessionPreference().getSessionId()?.let {
            val result = MovieApiService.api.getRatedMovies(it)
            logd(">>> $result")

            withContext(Dispatchers.Main) {
                listAdapter.submitList(result.results)
            }
        }

    }
}