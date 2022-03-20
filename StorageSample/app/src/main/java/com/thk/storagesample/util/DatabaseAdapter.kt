package com.thk.storagesample.util

import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteTransactionListener
import com.thk.storagesample.model.LogItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface DatabaseManager {
    suspend fun insert(content: String): Boolean
    suspend fun delete(number: Int): Boolean
    suspend fun modify(number: Int, newContent: String): Boolean
    suspend fun getAllLog(): List<LogItem>
}

class SqliteManager private constructor(context: Context) : DatabaseManager {
    private val dbHelper = SqliteHelper(context)
    private val database = dbHelper.writableDatabase

    companion object {
        private var sqliteManager: SqliteManager? = null
        fun getInstance(context: Context) = sqliteManager ?: SqliteManager(context)
    }

    override suspend fun insert(content: String) = withContext(Dispatchers.IO) {
        tryExecute {
            val query = "insert into ${LogTable.TABLE_NAME} (${LogTable.COLUMN_NAME_CONTENT}) values ('${content}');"
            database.execSQL(query)
        }
    }

    override suspend fun delete(number: Int) = withContext(Dispatchers.IO) {
        tryExecute {
            val query = "delete from ${LogTable.TABLE_NAME} where ${LogTable.COLUMN_NAME_NUMBER} = $number;"
            database.execSQL(query)
        }
    }

    override suspend fun modify(number: Int, newContent: String) = withContext(Dispatchers.IO) {
        tryExecute {
            val query = "update ${LogTable.TABLE_NAME} " +
                    "set ${LogTable.COLUMN_NAME_CONTENT} = '$newContent' " +
                    "where ${LogTable.COLUMN_NAME_NUMBER} = $number;"

            database.execSQL(query)
        }
    }

    override suspend fun getAllLog() = withContext(Dispatchers.IO) {
        val logs = mutableListOf<LogItem>()
        val query = "select * from ${LogTable.TABLE_NAME}"

        database.rawQuery(query, null).use {
            while (it.moveToNext()) {
                val number = it.getInt(it.getColumnIndexOrThrow(LogTable.COLUMN_NAME_NUMBER))
                val content = it.getString(it.getColumnIndexOrThrow(LogTable.COLUMN_NAME_CONTENT))

                logd(">>> num = $number content = $content")

                logs.add(LogItem(number, content))
            }
        }

        logs
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