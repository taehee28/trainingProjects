package com.thk.movieranking.network

import com.thk.movieranking.TMDB_BASE_URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * retrofit의 인스턴스와 작성한 인터페이스에 대한 구현체를 가지고 있는 오브젝트 클래스
 */
object MovieApiService {
    private val retrofit = Retrofit.Builder()
        .baseUrl(TMDB_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val _api = retrofit.create(MovieApiInterface::class.java)
    val api
        get() = _api
}
