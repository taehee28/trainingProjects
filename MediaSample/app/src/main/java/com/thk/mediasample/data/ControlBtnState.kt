package com.thk.mediasample.data

enum class ControlBtnState(
    val playBtn: Boolean,
    val pauseBtn: Boolean,
    val stopBtn: Boolean
) {
    LOADING(false, false, false),
    READY(true, false, false),
    PLAYING(false, true, true),
    PAUSED(true, false, true),
    STOPPED(true, false, false)
}