package com.thk.movieranking.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thk.movieranking.TMDB_IMAGE_URL
import com.thk.movieranking.databinding.ItemSearchRowBinding
import com.thk.movieranking.models.Movie
import com.thk.movieranking.utils.GlideApp

class SearchResultAdapter : ListAdapter<Movie, SearchResultAdapter.SearchResultViewHolder>(SearchResultDiff()) {

    var onViewClick: ((Int) -> Unit)? = null

    inner class SearchResultViewHolder(private val binding: ItemSearchRowBinding) : RecyclerView.ViewHolder(binding.root) {
        private var movieId: Int? = null

        init {
            binding.root.setOnClickListener {
                onViewClick?.invoke(movieId ?: -1)
            }
        }

        fun bind(item: Movie) {
            movieId = item.id

            item.posterPath?.let {
                GlideApp.with(binding.ivPoster)
                    .load(TMDB_IMAGE_URL + it)
                    .fitCenter()
                    .into(binding.ivPoster)
            }

            binding.run {
                tvTitle.text = item.title
                tvReleaseDate.text = item.releaseDate
            }
        }

        fun clearImage() {
            GlideApp.with(binding.ivPoster).clear(binding.ivPoster)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val binding = ItemSearchRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onViewRecycled(holder: SearchResultViewHolder) {
        holder.clearImage()
        super.onViewRecycled(holder)
    }
}

class SearchResultDiff : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
}