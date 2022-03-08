package com.thk.mediasample

import android.Manifest
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.thk.mediasample.data.RecordingBtnState
import com.thk.mediasample.databinding.FragmentAudioRecordBinding
import java.io.File

class AudioRecordFragment : Fragment() {
    private val TAG = AudioRecordFragment::class.simpleName

    private var _binding: FragmentAudioRecordBinding? = null
    private val binding
        get() = _binding!!

    private var recordPlayer: MediaPlayer? = null
    private var recorder: MediaRecorder? = null

    private val FILE_NAME = "recorded.mp4"
    private val outputPath: String by lazy {
        val appFilesDir = requireContext().filesDir
        val file = File(appFilesDir, FILE_NAME)
        file.absolutePath
    }

    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
        if (result.any { permission -> !permission.value }) {
            AlertDialog.Builder(requireContext())
                .setMessage("[설정] > [개인정보 보호] > [권한 관리자]에서 권한을 허락해주세요.")
                .setPositiveButton("확인") { dialoginterface, n ->
                    findNavController().navigateUp()
                    dialoginterface.dismiss()
                }
                .setCancelable(false)
                .show()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_audio_record, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeBtnState(RecordingBtnState.IDLE)

        permissionLauncher.launch(arrayOf(Manifest.permission.RECORD_AUDIO))

        binding.btnStartRecord.setOnClickListener { startRecord() }
        binding.btnStopRecord.setOnClickListener { stopRecord() }
        binding.btnPlayRecord.setOnClickListener { playRecord() }
        binding.btnStopPlayingRecord.setOnClickListener { stopPlayingRecord() }
        binding.btnRemoveFile.setOnClickListener { removeRecordFile() }
    }

    private fun startRecord() {
        if (recorder == null) recorder = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) MediaRecorder() else MediaRecorder(requireContext())

        recorder?.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)
            setOutputFile(outputPath)
        }

        tryAndCatch {
            recorder?.prepare()
            recorder?.start()
        }

        changeBtnState(RecordingBtnState.RECORDING)
    }

    private fun stopRecord() {
        tryAndCatch { recorder?.stop() }

        changeBtnState(RecordingBtnState.IDLE)
    }

    private fun playRecord() {
        val recordedFile = findRecordedFile()
        if (recordedFile == null) {
            showToast("파일이 없습니다.")
            return
        }

        if (recordPlayer == null) {
            recordPlayer = MediaPlayer().apply {
                setDataSource(recordedFile.absolutePath)
                setOnPreparedListener {
                    changeBtnState(RecordingBtnState.PLAYING)
                    it.start()
                }
                setOnCompletionListener {
                    changeBtnState(RecordingBtnState.IDLE)
                    it.stop()
                }
            }
        }
        recordPlayer?.prepare()
    }

    private fun stopPlayingRecord() {
        recordPlayer?.stop()
        changeBtnState(RecordingBtnState.IDLE)
    }

    private fun removeRecordFile() {
        val recordedFile = findRecordedFile()
        val deleteResult = recordedFile?.delete() ?: false

        showToast(
            if (deleteResult) "파일이 삭제되었습니다." else "파일이 없습니다."
        )
    }

    private fun changeBtnState(btnState: RecordingBtnState) {
        binding.btnState = btnState
    }

    private fun tryAndCatch(block: () -> Unit) {
        try {
            block()
        } catch (e: Exception) {
            Log.d(TAG, "Exception: ${e.message}")
        }
    }

    private fun findRecordedFile(): File? {
        val recordedFile: File?

        val resultFiles = requireContext().filesDir.listFiles { file, strName ->
            Log.d(TAG, "onViewCreated: $strName")
            strName == FILE_NAME
        }

        try {
            recordedFile = resultFiles?.first { it?.name == FILE_NAME }
        } catch (e: NoSuchElementException) {
            return null
        }

        return recordedFile
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onStop() {
        recorder?.release()
        recorder = null

        recordPlayer?.release()
        recordPlayer = null

        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}