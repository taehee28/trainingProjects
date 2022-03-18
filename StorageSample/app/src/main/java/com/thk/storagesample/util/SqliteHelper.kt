package com.thk.storagesample.util

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

object LogTable : BaseColumns {
    const val TABLE_NAME = "logs"
    const val COLUMN_NAME_NUMBER = "number"
    const val COLUMN_NAME_CONTENT = "content"
}

class SqliteHelper(
    context: Context,
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "logging.db"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val query = "create table if not exists ${LogTable.TABLE_NAME} (" +
                "${LogTable.COLUMN_NAME_NUMBER} integer primary key autoincrement," +
                "${LogTable.COLUMN_NAME_CONTENT} varchar(35)" +
                ");"

        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val query = "drop table if exists ${LogTable.TABLE_NAME}"

        db?.execSQL(query)
        onCreate(db)
    }
}