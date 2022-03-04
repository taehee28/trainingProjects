package com.thk.mediasample

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.thk.mediasample.databinding.FragmentAudioRecordBinding

class AudioRecordFragment : Fragment() {

    private lateinit var binding: FragmentAudioRecordBinding

    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (!isGranted){
            AlertDialog.Builder(requireContext())
                 .setMessage("[설정] > [개인정보 보호] > [권한 관리자]에서 저장공간 권한을 허락해주세요.")
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
        binding = FragmentAudioRecordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        permissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }


}