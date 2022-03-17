package com.thk.storagesample

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.FragmentActivity

object PreferenceKey {
    const val SETTING_PREFERENCE = "SETTING_PREFERENCE"

    const val KEY_SWITCH_VALUE = "KEY_SWITCH_VALUE"
    const val KEY_INPUT_STRING_VALUE = "KEY_INPUT_STRING_VALUE"
}

fun FragmentActivity.getSharedPreferencePrivate() = getSharedPreferences(PreferenceKey.SETTING_PREFERENCE, Context.MODE_PRIVATE)
fun FragmentActivity.getPreferencePrivate() = getPreferences(Context.MODE_PRIVATE)

fun SharedPreferences.setSwitchValue(isChecked: Boolean) = edit()?.run {
        putBoolean(PreferenceKey.KEY_SWITCH_VALUE, isChecked)
        apply()
    }

fun SharedPreferences.getSwitchValue() = getBoolean(PreferenceKey.KEY_SWITCH_VALUE, false)

fun SharedPreferences.setInputString(inputString: String) = edit()?.run {
    putString(PreferenceKey.KEY_INPUT_STRING_VALUE, inputString)
    apply()
}

fun SharedPreferences.getInputString() = getString(PreferenceKey.KEY_INPUT_STRING_VALUE, "null")

