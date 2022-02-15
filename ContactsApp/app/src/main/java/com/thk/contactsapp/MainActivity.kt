package com.thk.contactsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import com.thk.contactsapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val dataSet by lazy { getDataSet(this) }
    private val listAdapter by lazy { ContactListAdapter(dataSet) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.contactList.adapter = listAdapter
        val dividerItemDecoration = DividerItemDecoration(this, VERTICAL)
        binding.contactList.addItemDecoration(dividerItemDecoration)

        setSupportActionBar(binding.toolbar)
    }
}