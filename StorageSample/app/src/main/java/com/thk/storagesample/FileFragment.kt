package com.thk.storagesample

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.thk.storagesample.databinding.FragmentFileBinding
import com.thk.storagesample.util.isNotEmptyOrBlank
import com.thk.storagesample.util.logd
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.lang.Exception

object FilePath {
    const val TEXT_FILE_NAME = "myText.txt"
    const val COPIED_TEXT_FILE_NAME = "myText_copy.txt"
}


class FileFragment : BaseFragment<FragmentFileBinding>() {


    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentFileBinding {
        return FragmentFileBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnOk.setOnClickListener {
            val inputText = binding.textField.text.toString()
            if (!inputText.isNotEmptyOrBlank()) return@setOnClickListener

            writeTextIntoFile(inputText)
        }

        binding.btnLoad.setOnClickListener {
            readTextFromFile()
        }

        binding.btnRemove.setOnClickListener {
            removeFile()
        }

        binding.btnCopy.setOnClickListener {
            copyFile()
        }

        binding.btnTree.setOnClickListener {
            printTree()
        }

    }

    /**
     * 파일에 text 쓰기
     */
    private fun writeTextIntoFile(inputText: String) =  try {
        val file = File(requireActivity().filesDir, FilePath.TEXT_FILE_NAME)
        // /data/user/0/com.example.app/files/my_file.txt

        // 파일 없으면 생성
        if (!file.exists()) {
            file.createNewFile()
        }

        // 파일을 가르키는 File 인스턴스와 이어쓰기 여부 전달하면서
        // FileWirter 인스턴스 생성
        FileWriter(file, true).use {
            // 쓰고 줄바꿈 추가
            it.appendLine(inputText)
        }

    } catch (e: IOException) {
        e.printStackTrace()
        Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show()
    } finally {
        clearTextField()
    }

    private fun clearTextField() {
        binding.textField.run {
            setText("")
            clearFocus()
        }
    }

    /**
     * 파일에서 text 읽어오기
     */
    private fun readTextFromFile() {
        try {
            val file = File(requireActivity().filesDir, FilePath.TEXT_FILE_NAME)
            if (!file.exists()) {
                binding.tvContent.text = "no file!"
                return
            }

            // text 파일 가르키는 file 인스턴스 넘기면서
            // FilerReader 인스턴스 생성
            FileReader(file).use {
                val text = it.readText()
                binding.tvContent.text = text
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 파일 삭제
     */
    private fun removeFile() {
        try {
            val file = File(requireActivity().filesDir, FilePath.TEXT_FILE_NAME)

            // 파일 있으면 삭제
            if (file.exists()) {
                val result = file.delete()
                logd(">> delete $result")
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun copyFile() {
        val file = File(requireActivity().filesDir, FilePath.TEXT_FILE_NAME)
        if (!file.exists()) return

        val copiedFile = File(requireActivity().filesDir, FilePath.COPIED_TEXT_FILE_NAME)

        try {
            file.copyTo(copiedFile, true)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun printTree() {
        // /data/user/0/com.thk.storagesample 를 재귀적으로 순회 
        requireActivity().dataDir.walk().forEach { file -> logd(file.absolutePath) }
    }

    private fun printDirectory() {
        // 앱 별 저장소의 file 디렉터리
        // /data/user/0/com.thk.storagesample/files
        logd(requireActivity().filesDir.absolutePath)
        // 앱 별 저장소의 file 디렉터리의 상위 디렉터리
        // /data/user/0/com.thk.storagesample
        logd(requireActivity().dataDir.absolutePath)


        // 외부 저장소의 앱 별 저장소의 cache 디렉터리
        // /storage/emulated/0/Android/data/com.thk.storagesample/cache
        logd(requireActivity().externalCacheDir?.absolutePath!!)

        // 외부 저장소의 앱 별 저장소의 file 디렉터리
        // /storage/emulated/0/Android/data/com.thk.storagesample/files
        logd(requireActivity().getExternalFilesDir(null)?.absolutePath!!)

        // 외부 저장소의 앱 별 미디어폴더
        // /storage/emulated/0/Android/media/com.thk.storagesample
        for (file in requireActivity().externalMediaDirs) {
            logd(file.absolutePath)
        }
    }
}
