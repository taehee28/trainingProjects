package com.thk.movieranking.network

import com.thk.movieranking.BuildConfig
import com.thk.movieranking.models.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

interface MovieApiInterface {
    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "ko-KR",
        @Query("region") region: String = "KR"
    ): Call<MovieListResponse>

    /**
     * 현재 상영 중인 영화의 목록 가져오기
     */
    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "ko-KR",
        @Query("region") region: String = "KR"
    ): MovieListResponse

    /**
     * 영화 상세 정보 가져오기
     */
    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("language") language: String = "ko-KR"
    ) : Movie

    /**
     * 검색어에 맞는 영화 목록 가져오기
     */
    @GET("search/movie")
    suspend fun searchMovie(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("query") queryText: String,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "ko-KR",
        @Query("region") region: String = "KR"
    ) : MovieListResponse

    /**
     * 게스트 세션 아이디 얻기
     */
    @GET("authentication/guest_session/new")
    suspend fun getGuestSessionId(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ) : SessionInfo

    /**
     * 영화에 매긴 평점을 등록
     */
    @Headers("Content-Type:application/json;charset=utf-8")
    @POST("movie/{movie_id}/rating")
    suspend fun ratingMovie(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("guest_session_id") guestSessionId: String,
        @Body value: RatingValue
    ) : RequestResponse

    /**
     * 특정 게스트 세션 아이디로 평점을 매긴 영화들의 목록 가져오기
     */
    @GET("guest_session/{guest_session_id}/rated/movies")
    suspend fun getRatedMovies(
        @Path("guest_session_id") sessionId: String,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("language") language: String = "ko-KR",
        @Query("sort_by") sortBy: String = "created_at.desc"
    ) : MovieListResponse

    /**
     * 영화에 매긴 평점 지우기 요청
     */
    @DELETE("movie/{movie_id}/rating")
    suspend fun deleteRating(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("guest_session_id") guestSessionId: String,
    ) : RequestResponse
}