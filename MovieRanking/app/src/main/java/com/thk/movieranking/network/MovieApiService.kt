package com.thk.movieranking.network

import com.thk.movieranking.TMDB_BASE_URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MovieApiService {
    private val retrofit = Retrofit.Builder()
        .baseUrl(TMDB_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val _api = retrofit.create(MovieApiInterface::class.java)
    val api
        get() = _api
}

suspend fun getSessionId() = withContext(Dispatchers.IO) {
    val result = MovieApiService.api.getGuestSessionId()
    if (result.success) result.guestSessionId else null
}
