package com.thk.servicesample.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.thk.servicesample.util.KEY_RESULT
import com.thk.servicesample.util.logd
import com.thk.servicesample.util.sleep
import java.lang.IllegalArgumentException

class SumWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        logd(">> 작업 시작 ")

        // 이전 작업의 output Data를 input으로 받음
        val inputNum = inputData.getInt(KEY_RESULT, -1)
        var result = 0

        // 작업이 길어질 수 있도록 잠시동안 sleep
        sleep()

        return try {
            // 예외처리
            if (inputNum < 1) {
                throw IllegalArgumentException("input이 1보다 작음")
            }

            // 1부터 input까지 합 계산
            for (i in 1 until inputNum) {
                result += i
            }
            logd(">> result = $result")

            // 작업의 결과를 Data로 만듬
            val output = workDataOf(KEY_RESULT to result)

            // Data 넘기면서 Result 리턴
            Result.success(output)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            Result.failure()
        }
    }
}