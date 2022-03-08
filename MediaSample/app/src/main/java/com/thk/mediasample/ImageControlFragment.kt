package com.thk.mediasample

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.thk.mediasample.databinding.FragmentImageControlBinding
import com.thk.mediasample.util.GlideApp
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream
import java.io.File

class ImageControlFragment : Fragment() {
    private val TAG = ImageControlFragment::class.simpleName

    private var _binding: FragmentImageControlBinding? = null
    private val binding
        get() = _binding!!

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

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        loadPickedPhotoAsFile(result.data?.data)
    }

    private var photoFile: File? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentImageControlBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        permissionLauncher.launch(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE))

        displayFilePath(photoFile?.absolutePath)

        binding.btnLoadImg.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK).apply {
                type = MediaStore.Images.Media.CONTENT_TYPE
                type = "image/*"
            }
            galleryLauncher.launch(intent)
        }

        binding.btnCompress.setOnClickListener {
            if (photoFile == null) return@setOnClickListener

            CoroutineScope(Dispatchers.Default).launch {

                val compressedBitmap = compressBitmap()

                withContext(Dispatchers.Main) {
                    loadBitmapIntoImageView(compressedBitmap)
                }
                
            }

        }

        binding.btnSampling.setOnClickListener {
            photoFile?.let {
                val sampledBitmap = decodeSampledBitmap()
                loadBitmapIntoImageView(sampledBitmap)
            }
        }
    }

    private fun loadPickedPhotoAsFile(uri: Uri?) {
        uri?.let {
            CoroutineScope(Dispatchers.Default).launch {
                photoFile = GlideApp.with(requireContext()).asFile().load(it).submit().get()

                withContext(Dispatchers.Main) {
                    displayFilePath(photoFile?.absolutePath)
                }
            }
        }
    }

    private fun displayFilePath(path: String?) {
        binding.tvFilePath.text = path
    }

    private fun compressBitmap(): Bitmap {
        val outputStream = ByteArrayOutputStream()

        GlideApp.with(requireContext())
            .asBitmap()
            .load(photoFile)
            .submit()
            .get().run {
                compress(Bitmap.CompressFormat.JPEG, 10, outputStream)
                recycle()
            }

        return BitmapFactory.decodeByteArray(outputStream.toByteArray(), 0, outputStream.size())
    }

    private fun loadBitmapIntoImageView(bitmap: Bitmap) = GlideApp.with(requireContext()).load(bitmap).into(binding.imageView)

    private fun decodeSampledBitmap(): Bitmap {
        val resizedBitmap = BitmapFactory.Options().run {
            // 메모리에 비트맵을 로드하지 않고 정보만 가져오는 옵션
            inJustDecodeBounds = true

            BitmapFactory.decodeFile(photoFile?.toString(), this)
            printBitmapSize(outWidth, outHeight)

            inSampleSize = calculateInSampleSize(this, 100, 100)

            // 비트맵 리턴하도록 변경
            inJustDecodeBounds = false

            BitmapFactory.decodeFile(photoFile?.toString(), this)
        }

        printBitmapSize(resizedBitmap.width, resizedBitmap.height)

        return resizedBitmap

    }

    private fun printBitmapSize(width: Int, height: Int) {
        Log.d(
            TAG,
            """
                |>>>>> 
                |size: width = ${width}, 
                |      height = ${height}
            """.trimMargin()
        )
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // 이미지의 raw 넓이와 높이
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        // 초기값: 1배수
        var inSampleSize = 1

        // 원하는 크기보다 크면
        if (height > reqHeight || width > reqWidth) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            // 2의 제곱값이며, 해당 값으로 나눴을 때 높이와 넓이가
            // 요구된 크기보다 큰 값을 가지도록 하는 inSampleSize를 계산한다.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}