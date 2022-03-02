package com.thk.layoutsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputLayout
import com.thk.layoutsample.adapter.TodoListAdapter
import com.thk.layoutsample.data.TodoItem
import com.thk.layoutsample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.simpleName

    private lateinit var binding: ActivityMainBinding
    private lateinit var todoListAdapter: TodoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        todoListAdapter = TodoListAdapter(mutableListOf<TodoItem>())
        todoListAdapter.onItemLongClick = { position ->
            AlertDialog.Builder(this)
                .setItems(arrayOf("삭제하기")) { dialogInterface, listePos ->
                    todoListAdapter.removeTodoItem(position)
                }
                .show()
        }

        binding.rvTodoList.apply {
            adapter = todoListAdapter
        }

        binding.fabAddItem.setOnClickListener {
            AlertDialog.Builder(this)
                .setView(R.layout.dialog_textfield)
                .show()
                .also { alertDialog ->
                    if (alertDialog == null) return@also

                    val content = alertDialog.findViewById<TextInputLayout>(R.id.dialog_textInputLayout)?.editText?.text


                    alertDialog.findViewById<Button>(R.id.dialog_btn_add)?.setOnClickListener {
                        alertDialog.dismiss()
                        todoListAdapter.addTodoItem(content.toString())
                    }
                }
        }
    }
}