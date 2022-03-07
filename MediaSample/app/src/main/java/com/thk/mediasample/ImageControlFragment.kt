package com.thk.mediasample

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
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

    private var photoFile: File? = null

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
                val outputStream = ByteArrayOutputStream()

                GlideApp.with(requireContext())
                    .asBitmap()
                    .load(photoFile)
                    .submit()
                    .get().run {
                    compress(Bitmap.CompressFormat.JPEG, 10, outputStream)
                    recycle()
                }

                val compressedBitmap = BitmapFactory.decodeByteArray(outputStream.toByteArray(), 0, outputStream.size())

                withContext(Dispatchers.Main) {
                    GlideApp.with(requireContext()).load(compressedBitmap).into(binding.imageView)
                }
                
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}