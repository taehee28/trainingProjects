package com.thk.storagesample.util

import android.content.Context
import android.database.SQLException
import com.thk.storagesample.logd
import com.thk.storagesample.model.LogItem

interface DatabaseManager {
    fun insert(content: String): Boolean
    fun delete(number: Int): Boolean
    fun modify(number: Int, newContent: String): Boolean
    fun getAllLog(): List<LogItem>
}

class SqliteManager(context: Context) : DatabaseManager {
    private val dbHelper = SqliteHelper(context)
    private val database = dbHelper.writableDatabase

    override fun insert(content: String): Boolean = tryExecute {
        val query = "insert into ${LogTable.TABLE_NAME} (${LogTable.COLUMN_NAME_CONTENT}) values ('${content}');"
        database.execSQL(query)
    }

    override fun delete(number: Int): Boolean = tryExecute {
        val query = "delete from ${LogTable.TABLE_NAME} where ${LogTable.COLUMN_NAME_NUMBER} = $number"
        database.execSQL(query)
    }

    override fun modify(number: Int, newContent: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun getAllLog(): List<LogItem> {
        val query = "select * from ${LogTable.TABLE_NAME}"
        val cursor = database.use { it.rawQuery(query, null) }
//        while (cursor.moveToNext()) {
//        }
        logd(cursor.columnCount.toString())

        return listOf()
    }

    private fun tryExecute(block: () -> Unit): Boolean {
        return try {
            block()
            true
        } catch (e: SQLException) {
            e.printStackTrace()
            false
        }
    }
}