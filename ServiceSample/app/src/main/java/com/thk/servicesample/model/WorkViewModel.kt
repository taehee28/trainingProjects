package com.thk.servicesample.model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.thk.servicesample.util.WORK_TAG
import com.thk.servicesample.worker.GenerateNumberWorker
import java.lang.IllegalArgumentException

class WorkViewModel(application: Application) : ViewModel() {
    private val workManager = WorkManager.getInstance(application)
    val workInfos: LiveData<List<WorkInfo>>

    init {
        workInfos = workManager.getWorkInfosByTagLiveData(WORK_TAG)
    }

    fun generateRandomNumber() {
        val generateRequest = OneTimeWorkRequest.Builder(GenerateNumberWorker::class.java)
            .addTag(WORK_TAG)
            .build()

        workManager.enqueue(generateRequest)
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