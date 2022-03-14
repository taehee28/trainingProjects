package com.thk.servicesample.worker

import android.content.Context
import android.widget.Toast
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.thk.servicesample.util.KEY_RESULT
import com.thk.servicesample.util.logd
import com.thk.servicesample.util.sleep
import kotlin.random.Random

/**
 * 랜덤 정수값을 만들어내는 Worker
 */
class GenerateNumberWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        logd(">> 작업 시작 ")

        // 작업이 길어질 수 있도록 잠시동안 sleep
        sleep()

        return try {
            // 랜덤 정수 만들기
            val number = Random.nextInt(1, 100)
            logd(">> output = $number")

            // 작업의 결과값을 Data로 만듬
            val output = workDataOf(KEY_RESULT to number)

            // Data를 전달하면서 Result 리턴
            Result.success(output)
        } catch (e: IllegalArgumentException) {
            Result.failure()
        }
    }
}