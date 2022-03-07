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
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
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

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        loadCurrentPhotoInto(binding.imageView, result.data?.data)
    }

    private fun loadCurrentPhotoInto(view: ImageView, uri: Uri?) {
        uri?.let {
            CoroutineScope(Dispatchers.Default).launch {
                val pickedBitmap = GlideApp.with(view)
                    .asBitmap()
                    .override(Target.SIZE_ORIGINAL)
                    .load(it)
                    .submit().get()

                val outputStream = ByteArrayOutputStream()
                pickedBitmap.compress(Bitmap.CompressFormat.JPEG, 10, outputStream)
                val compressedBitmap = BitmapFactory.decodeByteArray(outputStream.toByteArray(), 0, outputStream.size())

                pickedBitmap.recycle()

                withContext(Dispatchers.Main) {
                    view.setImageBitmap(compressedBitmap)
                }
            }
        }
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

        binding.btnLoadImg.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK).apply {
                type = MediaStore.Images.Media.CONTENT_TYPE
                type = "image/*"
            }
            galleryLauncher.launch(intent)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}