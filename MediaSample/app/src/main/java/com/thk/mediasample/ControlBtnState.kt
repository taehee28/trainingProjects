package com.thk.mediasample

enum class ControlBtnState(
    val playBtn: Boolean,
    val pauseBtn: Boolean,
    val stopBtn: Boolean
) {
    NOT_READY(false, false, false),
    READY(true, false, false),
    PLAYING(false, true, true),
    PAUSED(true, false, true),
    STOPPED(true, false, false)
}