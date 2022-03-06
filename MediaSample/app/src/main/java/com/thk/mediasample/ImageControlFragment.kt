package com.thk.mediasample

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.thk.mediasample.databinding.FragmentImageControlBinding

class ImageControlFragment : Fragment() {
    private val TAG = ImageControlFragment::class.simpleName

    private var _binding: FragmentImageControlBinding? = null
    private val binding
        get() = _binding!!

    val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        Log.d(TAG, "result: ${result.data?.data}")
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