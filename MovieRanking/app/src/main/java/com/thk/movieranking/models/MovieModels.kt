package com.thk.movieranking.models

data class MovieListResponse(
    val page: Int,
    val results: List<Movie>
)

data class Movie(
    val id: Int,
    val title: String,
    val popularity: Double
)