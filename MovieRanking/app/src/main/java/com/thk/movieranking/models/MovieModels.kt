package com.thk.movieranking.models

import com.google.gson.annotations.SerializedName

data class MovieListResponse(
    val page: Int,
    val results: List<Movie>
)

data class Movie(
    val id: Int,
    val title: String,
    val popularity: Double,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("release_date")
    val releaseDate: String,
    val runtime: Int,
    val genres: List<Genre>,
    val overview: String?,
    @SerializedName("video")
    val hasVideo: Boolean
)

data class Genre(
    val id: Int,
    val name: String
)

data class Video(
    val site: String,
    val key: String
)