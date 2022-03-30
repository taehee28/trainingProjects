package com.thk.movieranking

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.fragment.app.FragmentActivity

inline fun <reified T> T.logd(message: String) = Log.d(T::class.java.simpleName, message)

fun FragmentActivity.getSessionPreference() = getSharedPreferences("session_preference", Context.MODE_PRIVATE)

fun SharedPreferences.getSessionId() = getString("key_sessionId", null)

fun SharedPreferences.putSessionId(sessionId: String) = edit()?.run {
    putString("key_sessionId", sessionId)
    apply()
}