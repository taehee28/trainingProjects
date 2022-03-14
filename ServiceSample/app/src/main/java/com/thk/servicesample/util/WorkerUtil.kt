package com.thk.servicesample.util

const val KEY_RESULT = "KEY_RESULT"

fun sleep() {
    try {
        Thread.sleep(3000, 0)
    } catch (e: InterruptedException) {
        e.printStackTrace()
    }
}