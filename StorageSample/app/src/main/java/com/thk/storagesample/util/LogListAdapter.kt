package com.thk.storagesample

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thk.storagesample.databinding.ItemLogBinding
import com.thk.storagesample.model.LogItem

class LogListAdapter : ListAdapter<LogItem, LogListAdapter.LogViewHolder>(LogItemDiffUtil()) {


    inner class LogViewHolder(private val binding: ItemLogBinding) : RecyclerView.ViewHolder(binding.root) {
        internal fun bind(item: LogItem) {
            binding.tvNumber.text = item.number.toString()
            binding.tvContent.text = item.content
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        val binding = ItemLogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LogViewHolder(binding = binding)
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class LogItemDiffUtil : DiffUtil.ItemCallback<LogItem>() {
    override fun areItemsTheSame(oldItem: LogItem, newItem: LogItem): Boolean {
        return oldItem.number == newItem.number
    }

    override fun areContentsTheSame(oldItem: LogItem, newItem: LogItem): Boolean {
        return oldItem.content == newItem.content
    }
}