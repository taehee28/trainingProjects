package com.thk.storagesample.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LogItem(
    @PrimaryKey(autoGenerate = true)
    val number: Int,

    @ColumnInfo(name = "content")
    val content: String
)
