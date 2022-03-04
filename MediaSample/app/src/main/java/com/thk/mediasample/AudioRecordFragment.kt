package com.thk.mediasample

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.Environment
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
import com.thk.mediasample.databinding.FragmentAudioRecordBinding
import java.io.File

class AudioRecordFragment : Fragment() {
    private val TAG = AudioRecordFragment::class.simpleName

    private lateinit var binding: FragmentAudioRecordBinding

    private val FILE_NAME = "recorded.mp4"
    private var recorder: MediaRecorder? = null
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
        binding =  FragmentAudioRecordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        permissionLauncher.launch(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO))

//        Log.d("testlog", ">>>>onViewCreated: ${requireContext().filesDir.listFiles()}")
//        for (file in requireContext().filesDir.listFiles()) {
//            Log.d(TAG, "onViewCreated: ${file.name}")
//        }
//
        binding.btnStartRecord.setOnClickListener {
            if (recorder == null) recorder = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) MediaRecorder() else MediaRecorder(requireContext())

            recorder?.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)
                setOutputFile(outputPath)
            }

            recorder?.prepare()
            recorder?.start()
        }

        binding.btnStopRecord.setOnClickListener {
            recorder?.stop()
        }

        binding.btnPlayRecord.setOnClickListener {
            val resultFiles = requireContext().filesDir.listFiles { file, strName ->
                Log.d(TAG, "onViewCreated: $strName")
                strName == FILE_NAME
            }
            for (file in resultFiles) {
                Log.d(TAG, "file : ${file.name}")
            }
        }
    }

    override fun onStop() {
        recorder?.release()
        recorder = null
        super.onStop()
    }

}