package com.thk.mediasample

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.thk.mediasample.databinding.FragmentImageControlBinding
import com.thk.mediasample.util.GlideApp

class ImageControlFragment : Fragment() {
    private val TAG = ImageControlFragment::class.simpleName

    private var _binding: FragmentImageControlBinding? = null
    private val binding
        get() = _binding!!

    private var currentPhotoUri: Uri? = null

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        currentPhotoUri = result.data?.data
        loadCurrentPhotoInto(binding.imageView)
    }

    private fun loadCurrentPhotoInto(view: ImageView) {
        currentPhotoUri?.let {
            GlideApp.with(view)
                .load(it)
                .centerInside()
                .into(view)
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