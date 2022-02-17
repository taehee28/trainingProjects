package com.thk.layoutsample

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.thk.layoutsample.databinding.ItemTodoBinding

class TodoListAdapter(
    private val todoItems: List<TodoItem>,
    private val onTodoCheckBoxClickListener: (Int, Boolean) -> Unit
) : RecyclerView.Adapter<TodoListAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TodoItem) {
            binding.tvContent.apply {
                text = item.content
                setTextColor(
                    Color.parseColor(
                        if (item.isCompleted) "#BDBDBD" else "#000000"
                    )
                )
            }

            binding.checkbox.isChecked = item.isCompleted
            binding.checkbox.setOnClickListener {
                val currentChecked = binding.checkbox.isChecked
                todoItems[layoutPosition].isCompleted = currentChecked
                onTodoCheckBoxClickListener(layoutPosition, currentChecked)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(todoItems[position])
    }

    override fun getItemCount(): Int = todoItems.size


}
