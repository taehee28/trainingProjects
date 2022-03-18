package com.thk.storagesample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.thk.storagesample.databinding.FragmentLoggingBinding
import com.thk.storagesample.model.LogItem
import com.thk.storagesample.util.DatabaseManager
import com.thk.storagesample.util.SqliteManager

class LoggingFragment : BaseFragment<FragmentLoggingBinding>() {

    private val listAdapter: LogListAdapter by lazy { LogListAdapter() }
    private val databaseManager: DatabaseManager by lazy { SqliteManager(requireContext()) }
    private var isModifyMode = false

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLoggingBinding {
        return FragmentLoggingBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            logListView.adapter = listAdapter
            btnOk.setOnClickListener { clickOk(isModifyMode) }
        }

    }

    private fun clickOk(modifyMode: Boolean) {
        val content = binding.textField.text.toString()
        kotlin.runCatching {
            check(content.isNotEmptyOrBlank()) { "비었음" }
        }.onSuccess {
            val result = databaseManager.insert(content)
            logd(result.toString())
        }.onFailure {
            it.printStackTrace()
            Toast.makeText(requireContext(), "입력되지 않았습니다.", Toast.LENGTH_SHORT).show()
        }
    }


}