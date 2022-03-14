package com.thk.servicesample.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.thk.servicesample.util.logd
import kotlinx.coroutines.delay

class SomeWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        repeat(3) {
            logd(">> do something....................")
            delay(1000)
        }

        logd(">> something is done!")

        return Result.success()
    }
}