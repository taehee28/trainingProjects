package com.thk.movieranking.network

import com.thk.movieranking.BuildConfig
import com.thk.movieranking.models.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

interface MovieApiInterface {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "ko-KR",
        @Query("region") region: String = "KR"
    ): MovieListResponse

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "ko-KR",
        @Query("region") region: String = "KR"
    ): MovieListResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("language") language: String = "ko-KR"
    ) : Movie

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("query") queryText: String,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "ko-KR",
        @Query("region") region: String = "KR"
    ) : MovieListResponse

    @GET("authentication/guest_session/new")
    suspend fun getGuestSessionId(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ) : SessionInfo

    @Headers("Content-Type:application/json;charset=utf-8")
    @POST("movie/{movie_id}/rating")
    suspend fun ratingMovie(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("guest_session_id") guestSessionId: String,
        @Body value: RatingValue
    ) : RequestResponse

    @GET("guest_session/{guest_session_id}/rated/movies")
    suspend fun getRatedMovies(
        @Path("guest_session_id") sessionId: String,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("language") language: String = "ko-KR",
        @Query("sort_by") sortBy: String = "created_at.desc"
    ) : MovieListResponse

    @DELETE("movie/{movie_id}/rating")
    suspend fun deleteRating(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("guest_session_id") guestSessionId: String,
    ) : RequestResponse
}