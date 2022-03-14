package com.thk.servicesample.util

// 작업의 Tag
const val WORK_TAG = "WORK_TAG"
const val GENERATE_AND_SUM_WORK_NAME = "GENERATE_AND_SUM_WORK_NAME"

const val WORK_NAME = "WORK_NAME"

// input/output Data의 Key로 쓰일 String
const val KEY_RESULT = "KEY_RESULT"

fun sleep() {
    try {
        Thread.sleep(3000, 0)
    } catch (e: InterruptedException) {
        e.printStackTrace()
    }
}