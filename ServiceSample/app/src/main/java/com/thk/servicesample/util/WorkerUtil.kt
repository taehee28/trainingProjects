package com.thk.servicesample.util

// 작업의 Tag
const val WORK_TAG = "WORK_TAG"
const val GENERATE_AND_SUM_WORK_TAG = "GENERATE_AND_SUM_WORK_TAG"

// input/output Data의 Key로 쓰일 String
const val KEY_RESULT = "KEY_RESULT"

fun sleep() {
    try {
        Thread.sleep(3000, 0)
    } catch (e: InterruptedException) {
        e.printStackTrace()
    }
}