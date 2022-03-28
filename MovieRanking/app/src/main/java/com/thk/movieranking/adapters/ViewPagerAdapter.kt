package com.thk.movieranking.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thk.movieranking.TMDB_IMAGE_URL
import com.thk.movieranking.databinding.ItemMovieCardBinding
import com.thk.movieranking.models.Movie
import com.thk.movieranking.utils.GlideApp

class ViewPagerAdapter(var items: List<Movie>) : RecyclerView.Adapter<ViewPagerAdapter.CardViewHolder>() {

    var onViewClick: ((Int) -> Unit)? = null

    inner class CardViewHolder(val binding: ItemMovieCardBinding) : RecyclerView.ViewHolder(binding.root) {
        private var movieId: Int? = null

        init {
            binding.run {
                ivPoster.setOnClickListener { onViewClick?.invoke(movieId ?: -1) }
                tvTitle.setOnClickListener { onViewClick?.invoke(movieId ?: -1) }
            }
        }

        fun bind(item: Movie) {
            movieId = item.id

            binding.tvTitle.text = item.title

            GlideApp.with(binding.ivPoster)
                .load(TMDB_IMAGE_URL + item.poster_path)
                .fitCenter()
                .into(binding.ivPoster)
        }

        fun clearImage() {
            GlideApp.with(binding.ivPoster).clear(binding.ivPoster)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = ItemMovieCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(item = items[position])
    }

    override fun onViewRecycled(holder: CardViewHolder) {
        holder.clearImage()
        super.onViewRecycled(holder)
    }

    override fun getItemCount() = items.size
}