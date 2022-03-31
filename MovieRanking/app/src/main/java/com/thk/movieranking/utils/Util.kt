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

val httpExceptionHandler = CoroutineExceptionHandler { _, throwable ->
    throwable.printStackTrace()
}

inline fun networkCoroutine(crossinline block: suspend () -> Unit) = CoroutineScope(Dispatchers.IO)
    .launch(httpExceptionHandler) {
        block()
    }