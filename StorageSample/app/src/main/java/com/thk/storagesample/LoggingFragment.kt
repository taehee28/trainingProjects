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

    // RecyclerView의 어댑터
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

    /**
     * Item 길게 눌렀을 때 호출 될 콜백
     */
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

    /**
     * 수정 모드 진입
     */
    private fun modifyLog(number: Int, content: String) {
        isModifyMode = true

        currentModifiedNumber = number
        binding.textField.setText(content)
    }

    /**
     * databaseManager에게 delete 요청하고 목록 다시 불러오기
     */
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

    /**
     * databaseManager에게 insert or update 요청하고 목록 다시 불러오기
     */
    private fun clickOk() = lifecycleScope.launch {
        val content = binding.textField.text.toString()

        try {

            check(content.isNotEmptyOrBlank()) { "입력이 없음" }

            // 현재 수정 중이면 update 요청
            val isSuccess = if (isModifyMode) {
                isModifyMode = false
                databaseManager.modify(currentModifiedNumber, content)
            } else {    // 아니면 insert 요청
                databaseManager.insert(content)
            }

            // 쿼리 성공 여부 확인
            check(isSuccess) { "query 실패" }

            // 목록 새로 불러오고나서 TextField 비우고 포커스 제거
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

    /**
     * databaseManager에게 모든 목록 select 요청, 화면에 목록 새로고침
     *
     * @param block 화면 목록 새로고침이 끝나면 실행할 코드 block
     */
    private suspend fun getAllLog(block: () -> Unit = {}) {
        val list = databaseManager.getAllLog()
        listAdapter.submitList(list) { block() }
    }


}