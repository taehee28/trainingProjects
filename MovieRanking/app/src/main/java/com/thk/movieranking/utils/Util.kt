package com.thk.movieranking

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

inline fun <reified T> T.logd(message: String) = Log.d(T::class.java.simpleName, message)

fun FragmentActivity.getSessionPreference() = getSharedPreferences("session_preference", Context.MODE_PRIVATE)

fun SharedPreferences.getSessionId() = getString("key_sessionId", null)

fun SharedPreferences.putSessionId(sessionId: String) = edit()?.run {
    putString("key_sessionId", sessionId)
    apply()
}

/**
 * 코루틴에서 발생하는 exception을 받기 위한 핸들러
 */
val httpExceptionHandler = CoroutineExceptionHandler { _, throwable ->
    throwable.printStackTrace()
}

/**
 * 예외처리 + IO 스레드 코루틴을 간편하게 사용하기 위한 함수
 */
inline fun networkCoroutine(crossinline block: suspend () -> Unit) = CoroutineScope(Dispatchers.IO)
    .launch(httpExceptionHandler) {
        block()
    }