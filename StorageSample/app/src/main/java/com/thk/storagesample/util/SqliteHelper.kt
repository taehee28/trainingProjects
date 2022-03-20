package com.thk.storagesample.util

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SqliteHelper(
    context: Context,
) : SQLiteOpenHelper(context, DatabaseInfo.DATABASE_NAME, null, DatabaseInfo.DATABASE_VERSION) {

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