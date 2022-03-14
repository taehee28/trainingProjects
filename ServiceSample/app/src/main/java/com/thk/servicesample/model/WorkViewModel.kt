package com.thk.servicesample.model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import com.thk.servicesample.util.GENERATE_AND_SUM_WORK_NAME
import com.thk.servicesample.util.WORK_TAG
import com.thk.servicesample.worker.GenerateNumberWorker
import com.thk.servicesample.worker.SomeWorker
import com.thk.servicesample.worker.SumWorker
import java.lang.IllegalArgumentException
import java.util.*
import java.util.concurrent.TimeUnit

class WorkViewModel(application: Application) : ViewModel() {
    private val workManager = WorkManager.getInstance(application)

    // 작업에 대한 정보인 WorkInfo를 가지는 LiveData
    internal val workInfos: LiveData<List<WorkInfo>> = workManager.getWorkInfosByTagLiveData(WORK_TAG)

    /**
     * 랜덤값을 생성하는 Worker 실행 
     */
    fun generateRandomNumber() {
        // 구현한 Worker로 WorkRequest 생성
        val generateRequest = OneTimeWorkRequest.Builder(GenerateNumberWorker::class.java)
            .addTag(WORK_TAG)
            .build()

        // 일반적인 작업 등록
        workManager.enqueue(generateRequest)
    }

    /**
     * 랜덤값을 만드는 Worker와 값을 받아 1부터 값까지 더하는 Worker를 이어서 실행
     */
    fun generateAndSum() {
        val generateRequest = OneTimeWorkRequestBuilder<GenerateNumberWorker>()
            .build()

        // 제약조건
        // 제약조건을 만족하지 않으면 Worker를 중지하고 조건이 충족될 때 실행
        val constraints = Constraints.Builder()
            .setRequiresCharging(true)  // 충전 중 일때만 실행
            .build()

        val sumRequest = OneTimeWorkRequestBuilder<SumWorker>()
            .addTag(WORK_TAG)
            .setConstraints(constraints)    // 제약조건 추가
            .build()

        // 여러 작업을 순서대로 실행하도록 체이닝
        // 한번에 하나의 작업만 실행하도록 고유 작업으로 만들어줌
        workManager.beginUniqueWork(GENERATE_AND_SUM_WORK_NAME, ExistingWorkPolicy.REPLACE, generateRequest)
            .then(sumRequest)
            .enqueue()
    }

    /**
     * 코루틴인 Worker 실행
     */
    fun coroutineWork() {
        workManager.enqueue(OneTimeWorkRequest.from(SomeWorker::class.java))
    }

    fun periodWork() {
        // TODO: 반복횟수 지정 찾아보기
        val request = PeriodicWorkRequestBuilder<SomeWorker>(2000, TimeUnit.MILLISECONDS).build()

        workManager.enqueue(request)
    }

    fun parallelWork() {

    }

    fun combineWork() {

    }

    fun cancelWork() {
        // 태그로 작업들을 식별해서 전부 취소
        workManager.cancelAllWorkByTag(WORK_TAG)
    }

    fun pruneWork() {
        // final state(SUCCEED, FAILED, CANCELED)인 작업들을 제거
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