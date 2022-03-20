package com.thk.storagesample.util

import android.provider.BaseColumns

object LogTable : BaseColumns {
    const val TABLE_NAME = "logs"
    const val COLUMN_NAME_NUMBER = "number"
    const val COLUMN_NAME_CONTENT = "content"
}

object DatabaseInfo {
    const val DATABASE_VERSION = 1
    const val DATABASE_NAME = "logging.db"
    const val DATABASE_NAME_ROOM = "logging_room.db"
}