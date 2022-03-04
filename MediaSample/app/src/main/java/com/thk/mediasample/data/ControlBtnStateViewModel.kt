package com.thk.mediasample.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ControlBtnStateViewModel : ViewModel() {
    private val TAG = ControlBtnStateViewModel::class.simpleName

    private val _btnState = MutableLiveData<ControlBtnState>(ControlBtnState.LOADING)
    val btnState: LiveData<ControlBtnState>
        get() = _btnState

    fun changeBtnState(state: ControlBtnState) {
        Log.d(TAG, "changeBtnState: ${state.name}")
        _btnState.value = state
    }
}