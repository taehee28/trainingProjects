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

data class SessionInfo(
    val success: Boolean,
    @SerializedName("guest_session_id")
    val guestSessionId: String,
    @SerializedName("expires_at")
    val expiresAt: String
)

data class RatingValue(
    val value: Double
)

data class RequestResponse(
    @SerializedName("status_code")
    val statusCode: Int,
    @SerializedName("status_message")
    val statusMessage: String
)