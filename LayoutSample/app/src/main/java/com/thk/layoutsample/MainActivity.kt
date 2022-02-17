package com.thk.layoutsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.thk.layoutsample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var todoListAdapter: TodoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        todoListAdapter = TodoListAdapter(testItems) { position, isChecked ->
            todoListAdapter.notifyItemChanged(position)
        }

        binding.rvTodoList.apply {
            adapter = todoListAdapter
        }
    }
}