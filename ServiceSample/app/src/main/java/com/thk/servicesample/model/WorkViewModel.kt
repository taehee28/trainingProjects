package com.thk.servicesample.model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import com.thk.servicesample.util.GENERATE_AND_SUM_WORK_NAME
import com.thk.servicesample.util.WORK_NAME
import com.thk.servicesample.util.WORK_TAG
import com.thk.servicesample.worker.GenerateNumberWorker
import com.thk.servicesample.worker.SumWorker
import java.lang.IllegalArgumentException
import java.util.*

class WorkViewModel(application: Application) : ViewModel() {
    private val workManager = WorkManager.getInstance(application)
    internal val workInfos: LiveData<List<WorkInfo>> = workManager.getWorkInfosByTagLiveData(WORK_TAG)


    fun generateRandomNumber() {
        val generateRequest = OneTimeWorkRequest.Builder(GenerateNumberWorker::class.java).addTag(WORK_TAG).build()
        workManager.enqueue(generateRequest)
    }

    fun generateAndSum() {
        val generateRequest = OneTimeWorkRequestBuilder<GenerateNumberWorker>()
            .build()

        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .build()

        val sumRequest = OneTimeWorkRequestBuilder<SumWorker>()
            .addTag(WORK_TAG)
            .setConstraints(constraints)
            .build()

        workManager.beginUniqueWork(GENERATE_AND_SUM_WORK_NAME, ExistingWorkPolicy.REPLACE, generateRequest)
            .then(sumRequest)
            .enqueue()
    }

    fun cancelWork() {
        workManager.cancelAllWorkByTag(WORK_TAG)
    }

    fun pruneWork() {
        workManager.pruneWork()
    }

    class WorkViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(WorkViewModel::class.java)) {
                WorkViewModel(application) as T
            } else {
                throw IllegalArgumentException()
            }
        }
    }
}