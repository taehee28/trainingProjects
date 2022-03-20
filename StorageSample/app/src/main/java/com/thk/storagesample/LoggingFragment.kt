package com.thk.storagesample

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.thk.storagesample.databinding.FragmentLoggingBinding
import com.thk.storagesample.util.*
import kotlinx.coroutines.launch
import java.lang.IllegalStateException

class LoggingFragment : BaseFragment<FragmentLoggingBinding>() {

    private val listAdapter: LogListAdapter by lazy { LogListAdapter() }

    // SQLite <-> Room 데이터베이스 변경 
    private val databaseManager: DatabaseManager by lazy { SqliteManager.getInstance(requireContext()) }
//    private val databaseManager: DatabaseManager by lazy { RoomManager.getInstance(requireContext()) }

    private var isModifyMode = false
    private var currentModifiedNumber = -1

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLoggingBinding {
        return FragmentLoggingBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            logListView.adapter = listAdapter.apply { onItemLongClick = this@LoggingFragment::showItemMenu }
            btnOk.setOnClickListener { clickOk() }
        }


        lifecycleScope.launch {
            getAllLog()
        }

    }

    private fun showItemMenu(number: Int?, content: String?) {
        if (number == null || content == null) return

        AlertDialog.Builder(requireContext())
            .setItems(arrayOf("수정하기", "삭제하기")) { dialog, which ->
                when (which) {
                    0 -> modifyLog(number, content)
                    1 -> deleteLog(number)
                }
            }
            .show()

    }

    private fun modifyLog(number: Int, content: String) {
        isModifyMode = true

        currentModifiedNumber = number
        binding.textField.setText(content)
    }

    private fun deleteLog(number: Int) = lifecycleScope.launch {
        try {
            val isSuccess = databaseManager.delete(number)
            check(isSuccess) { "query 실패" }

            getAllLog()

        } catch (e: IllegalStateException) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show()
        }

    }

    private fun clickOk() = lifecycleScope.launch {
        val content = binding.textField.text.toString()

        try {

            check(content.isNotEmptyOrBlank()) { "비었음" }

            val isSuccess = if (isModifyMode) {
                isModifyMode = false
                databaseManager.modify(currentModifiedNumber, content)
            } else {
                databaseManager.insert(content)
            }

            check(isSuccess) { "query 실패" }

            getAllLog {
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

    private suspend fun getAllLog(block: () -> Unit = {}) {
        val list = databaseManager.getAllLog()
        listAdapter.submitList(list) { block() }
    }


}