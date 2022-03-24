package com.thk.storagesample

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.FileOutputStream
import java.io.FileWriter
import java.util.*

class MediaStoreFragment : BaseFragment<FragmentMediastoreBinding>() {

    /**
     * 퍼미션 런처 콜백
     */
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (!it) {
            Toast.makeText(requireContext(), "거부됨", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * selector로 선택한 이미지에 대한 콜백 등록
     */
    private val imageSelectorLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        logd(">> picked uri = $uri")

        // 선택한 이미지를 이미지뷰에 띄우기
        uri?.let { binding.ivPhoto.setImageURI(it) }
    }

    /**
     * selector로 생성한 파일에 대한 콜백 등록
     */
    private val createSelectorLauncher = registerForActivityResult(
        ActivityResultContracts.CreateDocument()
    ) { uri ->
        logd(">> created file uri = $uri")

        uri ?: return@registerForActivityResult

        // 얻은 Uri에 데이터를 쓰려면 FileDescriptor 사용
        requireContext().contentResolver.openFileDescriptor(uri, "w").use {
            FileWriter(it!!.fileDescriptor).use { fileWriter ->
                fileWriter.write("hello world!")
            }
        }
    }

    /**
     * selector로 선택한 파일 삭제하는 콜백 등록
     */
    private val removeSelectorLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            // DocumentsContract를 통해 파일 삭제
            DocumentsContract.deleteDocument(requireContext().contentResolver, uri)
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

        binding.btnOpenSaf.setOnClickListener {
            openSelector()
        }

        binding.btnCreateFile.setOnClickListener {
            openSelectorToCreateFile()
        }

        binding.btnRemoveFile.setOnClickListener {
            openSelectorToRemoveFile()
        }

    }

    /**
     * 최근 사진을 쿼리하여 화면에 표시하는 메서드
     */
    private fun loadRecentPic() {
        // 쿼리로 받고싶은 항목(컬럼)에 대한 배열
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_TAKEN
        )

        // 쿼리 결과를 Cursor로 받음
        val cursor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {  // 버전에 따른 분기처리

            // 쿼리의 조건을 bundle로 생성
            val bundle = bundleOf(
                ContentResolver.QUERY_ARG_LIMIT to 1,
                ContentResolver.QUERY_ARG_SORT_COLUMNS to arrayOf(MediaStore.Images.Media.DATE_TAKEN),
                ContentResolver.QUERY_ARG_SORT_DIRECTION to ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
            )

            // Images에 해당하는 파일들 쿼리하여 Cursor 리턴
            requireContext().contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,   // Images URI
                projection,
                bundle,
                null
            )
        } else {

            // 쿼리 조건
            val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC limit 1"

            // Images에 해당하는 파일들 쿼리하여 Cursor 리턴
            requireContext().contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,   // Image URI
                projection,
                null,
                null,
                sortOrder
            )
        }

        // 얻고자 하는 파일의 URI를 담을 변수
        var contentUri: Uri? = null

        cursor?.use {
            // 컬럼의 이름을 넘겨서 컬럼의 인덱스 번호를 획득
            val idColumnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val displayNameColumnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val dateTakenColumnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)

            logd(it.count.toString())

            while (it.moveToNext()) {
                // 컬럼의 인덱스를 넘겨서 컬럼의 값 획득
                val dateTaken = Date(it.getLong(dateTakenColumnIndex))
                val displayName = it.getString(displayNameColumnIndex)
                val id = it.getLong(idColumnIndex)

                // 파일의 최종 URI는 쿼리한 위치의 URI에 파일의 id 값을 붙여서 완성
                contentUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id.toString())

                logd(">> query result = date: $dateTaken, displayName: $displayName, uri: $contentUri")
            }
        }

        // 가져온 이미지 파일의 URI를 사용해서 이미지뷰에 이미지 표시
        binding.ivPhoto.setImageURI(contentUri)
    }

    /**
     * 프로젝트의 res/raw 폴더에 있는 이미지를 기기에 저장하는 메서드
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveRawImage() = CoroutineScope(Dispatchers.IO).launch {

        // 기기에 저장하고자 하는 파일의 정보를 정의
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "saved_raw_image.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "Image/jpg")
            put(MediaStore.Images.Media.IS_PENDING, 1)  // 해당 파일에 접근하려는 다른 요청들을 무시하는 옵션
        }

        // 파일을 저장하고자 하는 위치의 URI 획득
        val collection: Uri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)

        // 저장하고자 하는 위치에 정의해놓은 파일 정보를 가지고 파일을 생성(insert)하고, 해당 파일의 URI를 획득
        val item: Uri = requireContext().contentResolver.insert(collection, values)!!

        // 추상화된 URI를 사용할 수 있게 해주는 FileDescriptor를 쓰기모드로 생성
        requireContext().contentResolver.openFileDescriptor(item, "w").use {

            // FileDescriptor를 통해 파일에 데이터를 쓸 수 있는 FileOutputStream을 생성
            FileOutputStream(it!!.fileDescriptor).use { outputStream ->

                // 프로젝트의 raw 이미지 파일을 불러오는 InputStream 생성
                resources.openRawResource(R.raw.image_dog).use { imageInputStream ->

                    // InputStream으로 읽어들인 데이터를 outputStram을 통해 파일에 데이터 쓰기
                    while (true) {
                        val data = imageInputStream.read()
                        if (data == -1) break

                        outputStream.write(data)
                    }
                }
            }
        }

        values.clear()
        values.put(MediaStore.Images.Media.IS_PENDING, 0)   // write가 끝났으니 다른 곳에서 접근할 수 있도록 허용
        requireContext().contentResolver.update(item, values, null, null)   // 허용한걸 반영
    }

    /**
     * SAF의 selector 열기
     */
    private fun openSelector() {
        imageSelectorLauncher.launch(
            arrayOf(
                "image/*"   // 접근 가능하게 할 파일 타입 명시
            )
        )
    }

    private fun openSelectorToCreateFile() {
        // 새로 생성할 파일의 이름
        val newFileName = "new_file.txt"

        createSelectorLauncher.launch(newFileName)
    }

    private fun openSelectorToRemoveFile() {
        removeSelectorLauncher.launch(
            arrayOf(
                "image/*"    // 접근 가능하게 할 파일 타입 명시
            )
        )
    }
}