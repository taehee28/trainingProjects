package com.thk.storagesample

import android.util.Log
import android.view.View
import androidx.annotation.IdRes
import androidx.navigation.findNavController

inline fun <reified T> T.logd(message: String) = Log.d(T::class.java.simpleName, message)

fun View.navigate(@IdRes actionId: Int) = findNavController().navigate(actionId)