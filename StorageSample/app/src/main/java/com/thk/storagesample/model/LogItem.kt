package com.thk.storagesample.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * logging 관련 공통적으로 사용되는 data class
 */
@Entity
data class LogItem(
    @PrimaryKey(autoGenerate = true)
    val number: Int,

    @ColumnInfo(name = "content")
    val content: String
)
