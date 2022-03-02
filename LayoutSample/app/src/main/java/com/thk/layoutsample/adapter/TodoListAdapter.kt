package com.thk.layoutsample.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thk.layoutsample.data.TodoItem
import com.thk.layoutsample.databinding.ItemTodoBinding

class TodoListAdapter(
    private val todoItems: MutableList<TodoItem>
) : RecyclerView.Adapter<TodoListAdapter.ViewHolder>() {

    var onItemLongClick: ((Int) -> Unit)? = null

    inner class ViewHolder(val binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnLongClickListener {
                onItemLongClick?.invoke(adapterPosition)
                true
            }

            binding.checkbox.setOnClickListener {
                val currentChecked = binding.checkbox.isChecked
                todoItems[layoutPosition].isCompleted = currentChecked
                notifyItemChanged(layoutPosition)
            }
        }

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

    fun addTodoItem(content: String) {
        todoItems.add(TodoItem(content))
        notifyItemInserted(todoItems.size - 1)
    }

    fun removeTodoItem(position: Int) {
        todoItems.removeAt(position)
        notifyItemRemoved(position)
    }


}
