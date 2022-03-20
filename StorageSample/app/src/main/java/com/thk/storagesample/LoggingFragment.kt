package com.thk.storagesample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.thk.storagesample.databinding.FragmentLoggingBinding
import com.thk.storagesample.model.LogItem
import com.thk.storagesample.util.DatabaseManager
import com.thk.storagesample.util.LogListAdapter
import com.thk.storagesample.util.SqliteManager
import com.thk.storagesample.util.isNotEmptyOrBlank
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.IllegalStateException

class LoggingFragment : BaseFragment<FragmentLoggingBinding>() {

    private val listAdapter: LogListAdapter by lazy { LogListAdapter() }
    private val databaseManager: DatabaseManager by lazy { SqliteManager.getInstance(requireContext()) }
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
            btnOk.setOnClickListener { clickOk() }
        }

        getAllLog()

    }

    private fun clickOk() = lifecycleScope.launch {
        val content = binding.textField.text.toString()

        try {

            check(content.isNotEmptyOrBlank()) { "비었음" }

            val isSuccess = databaseManager.insert(content)
            check(isSuccess) { "insert 실패" }

            val list = databaseManager.getAllLog()
            listAdapter.submitList(list) {
                binding.textField.run {
                    setText("")
                    clearFocus()
                }
            }

        } catch (e: IllegalStateException) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getAllLog() = lifecycleScope.launch {
        val list = databaseManager.getAllLog()
        listAdapter.submitList(list)
    }


}