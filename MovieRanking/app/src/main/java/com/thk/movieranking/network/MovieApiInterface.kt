package com.thk.movieranking.network

import com.thk.movieranking.BuildConfig
import com.thk.movieranking.models.Movie
import com.thk.movieranking.models.MovieListResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

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
}