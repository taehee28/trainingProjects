package com.thk.movieranking.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thk.movieranking.TMDB_IMAGE_URL
import com.thk.movieranking.databinding.ItemRatedRowBinding
import com.thk.movieranking.models.Movie
import com.thk.movieranking.utils.GlideApp

class RatedListAdapter : ListAdapter<Movie, RatedListAdapter.RatedMovieViewHolder>(RatedListDiff()) {
    var onViewClick: ((Int) -> Unit)? = null

    inner class RatedMovieViewHolder(private val binding: ItemRatedRowBinding) : RecyclerView.ViewHolder(binding.root) {
        var movieId: Int? = null

        init {
            binding.root.setOnClickListener {
                onViewClick?.invoke(movieId ?: -1)
            }
        }

        fun bind(item: Movie) {
            movieId = item.id

            GlideApp.with(binding.ivPoster)
                .load(TMDB_IMAGE_URL + item.posterPath)
                .fitCenter()
                .into(binding.ivPoster)

            binding.run {
                tvTitle.text = item.title
                tvRateValue.text = item.rating.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatedMovieViewHolder {
        val binding = ItemRatedRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RatedMovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RatedMovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class RatedListDiff : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return (oldItem.title == newItem.title) and (oldItem.rating == newItem.rating)
    }
}