package com.thk.servicesample.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CountViewModel : ViewModel() {
    private var _count = MutableLiveData(0)
    val count: LiveData<Int>
        get() = _count

    fun countUp() {
        _count.value = _count.value?.plus(1)
    }
}