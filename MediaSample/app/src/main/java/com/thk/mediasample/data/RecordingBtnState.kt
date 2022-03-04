package com.thk.mediasample.data

enum class RecordingBtnState(
    val recordBtn: Boolean,
    val stopRecordBtn: Boolean,
    val playRecordBtn: Boolean,
    val stopPlayingBtn: Boolean,
    val removeBtn: Boolean
) {
    IDLE(true, false, true, false, true),
    RECORDING(false, true, false, false, false),
    PLAYING(false, false, false, true, false)
}