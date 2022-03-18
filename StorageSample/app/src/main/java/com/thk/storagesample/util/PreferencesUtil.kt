package com.thk.storagesample.util

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.FragmentActivity

object PreferenceKey {
    internal const val USER_INFO_PREFERENCE = "USER_INFO_PREFERENCE"

    const val KEY_USER_NAME = "KEY_USER_NAME"
    const val KEY_USER_EMAIL = "KEY_USER_EMAIL"
    const val KEY_USER_AGE = "KEY_USER_AGE"
}

fun FragmentActivity.getUserInfoPreference() = getSharedPreferences(PreferenceKey.USER_INFO_PREFERENCE, Context.MODE_PRIVATE)

fun SharedPreferences.setUserInfo(userInfo: Triple<String, String, Int>) = edit()?.run {
    val (name, email, age) = userInfo
    putString(PreferenceKey.KEY_USER_NAME, name)
    putString(PreferenceKey.KEY_USER_EMAIL, email)
    putInt(PreferenceKey.KEY_USER_AGE, age)
    commit()
}

fun SharedPreferences.getUserInfo() = kotlin.run {
    val name = getString(PreferenceKey.KEY_USER_NAME, "null")
    val email = getString(PreferenceKey.KEY_USER_EMAIL, "null")
    val age = getInt(PreferenceKey.KEY_USER_AGE, -1)

    Triple(name, email, age)
}

