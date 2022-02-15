package com.thk.contactsapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thk.contactsapp.databinding.ItemContactBinding

class ContactListAdapter(private val dataSet: List<Contact>) : RecyclerView.Adapter<ContactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(dataSet[position])
    }

    override fun getItemCount() = dataSet.size

    override fun onViewRecycled(holder: ContactViewHolder) {
        holder.clearLoadedImage()
        super.onViewRecycled(holder)
    }

}

class ContactViewHolder(private val binding: ItemContactBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(data: Contact) {
        GlideApp
            .with(binding.contactProfilePic)
            .load(data.profilePic)
            .circleCrop()
            .into(binding.contactProfilePic)

        binding.contactName.text = data.name
        binding.contactPhoneNum.text = data.phoneNum
    }

    fun clearLoadedImage() {
        GlideApp.with(binding.contactProfilePic).clear(binding.contactProfilePic)
    }

}
