package com.thk.storagesample

import android.content.ContentResolver
import android.content.ContentValues
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import com.thk.storagesample.databinding.FragmentMediastoreBinding
import com.thk.storagesample.util.logd
import java.io.FileOutputStream
import java.util.*

class MediaStoreFragment : BaseFragment<FragmentMediastoreBinding>() {

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (!it) {
            Toast.makeText(requireContext(), "거부됨", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMediastoreBinding {
        return FragmentMediastoreBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)

        binding.btnLoadPic.setOnClickListener {
            loadRecentPic()
        }

        binding.btnSaveRawPic.setOnClickListener {
            if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.Q) {
                saveRawImage()
            }
        }

    }

    private fun loadRecentPic() {
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_TAKEN
        )


        val cursor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val bundle = bundleOf(
                ContentResolver.QUERY_ARG_LIMIT to 1,
                ContentResolver.QUERY_ARG_SORT_COLUMNS to arrayOf(MediaStore.Images.Media.DATE_TAKEN),
                ContentResolver.QUERY_ARG_SORT_DIRECTION to ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
            )

            requireContext().contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                bundle,
                null
            )
        } else {
            val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC limit 1"

            requireContext().contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                sortOrder
            )
        }

        var contentUri: Uri? = null

        cursor?.use {
            val idColumnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val displayNameColumnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val dateTakenColumnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)

            logd(it.count.toString())

            while (it.moveToNext()) {
                val dateTaken = Date(it.getLong(dateTakenColumnIndex))
                val displayName = it.getString(displayNameColumnIndex)
                val id = it.getLong(idColumnIndex)
                contentUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id.toString())

                logd(">> query result = date: $dateTaken, displayName: $displayName, uri: $contentUri")
            }
        }

        binding.ivPhoto.setImageURI(contentUri)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveRawImage() {
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "saved_raw_image.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "Image/jpg")
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        val item = requireContext().contentResolver.insert(collection, values)!!

        requireContext().contentResolver.openFileDescriptor(item, "w", null).use {
            FileOutputStream(it!!.fileDescriptor).use { outputStream ->
                resources.openRawResource(R.raw.image_dog).use { imageInputStream ->
                    while (true) {
                        val data = imageInputStream.read()
                        if (data == -1) break

                        outputStream.write(data)
                    }
                }
            }
        }

        values.clear()
        values.put(MediaStore.Images.Media.IS_PENDING, 0)
        requireContext().contentResolver.update(item, values, null, null)
    }
}