package com.thk.mediasample

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BtnStateViewModel : ViewModel() {
    private val TAG = BtnStateViewModel::class.simpleName

    private val _btnState = MutableLiveData<ControlBtnState>(ControlBtnState.READY)
    val btnState: LiveData<ControlBtnState>
        get() = _btnState

    fun changeBtnState(state: ControlBtnState) {
        Log.d(TAG, "changeBtnState: ${state.name}")
        _btnState.value = state
    }
}