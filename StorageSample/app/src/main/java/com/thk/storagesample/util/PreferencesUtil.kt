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

/**
 * User Info의 Preference를 간편하게 가져오기 위한 확장함수
 */
fun FragmentActivity.getUserInfoPreference() = getSharedPreferences(PreferenceKey.USER_INFO_PREFERENCE, Context.MODE_PRIVATE)

/**
 * SharedPreference에 값 쓰기와 반영을 한번에 하기 위한 확장함수
 *
 */
fun SharedPreferences.setUserInfo(name: String, email: String, age: Int) = edit()?.run {
    putString(PreferenceKey.KEY_USER_NAME, name)
    putString(PreferenceKey.KEY_USER_EMAIL, email)
    putInt(PreferenceKey.KEY_USER_AGE, age)
    commit()
}

/**
 * SharedPreference에서 특정 값들을 한번에 가져오기 위한 확장함수
 *
 * @return name, email, age가 담긴 Trple 객체
 */
fun SharedPreferences.getUserInfo() = kotlin.run {
    val name = getString(PreferenceKey.KEY_USER_NAME, "null")
    val email = getString(PreferenceKey.KEY_USER_EMAIL, "null")
    val age = getInt(PreferenceKey.KEY_USER_AGE, -1)

    Triple(name, email, age)
}

