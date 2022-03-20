package com.thk.storagesample.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thk.storagesample.databinding.ItemLogBinding
import com.thk.storagesample.model.LogItem

class LogListAdapter : ListAdapter<LogItem, LogListAdapter.LogViewHolder>(LogItemDiffUtil()) {

    var onItemLongClick: ((Int?, String?) -> Unit)? = null

    inner class LogViewHolder(private val binding: ItemLogBinding) : RecyclerView.ViewHolder(binding.root) {
        private var logItem: LogItem? = null

        init {
            binding.root.setOnLongClickListener {
                onItemLongClick?.invoke(logItem?.number, logItem?.content)
                true
            }
        }

        internal fun bind(item: LogItem) {
            logItem = item

            binding.run {
                tvNumber.text = item.number.toString()
                tvContent.text = item.content
            }
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