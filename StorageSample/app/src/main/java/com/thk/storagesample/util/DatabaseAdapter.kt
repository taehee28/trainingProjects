package com.thk.storagesample.util

import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteTransactionListener
import androidx.room.Room
import com.thk.storagesample.model.LogItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface DatabaseManager {
    /**
     * Coroutine에서 insert 수행
     *
     * @param content 새로 삽입할 Log의 내용
     * @return 성공 여부
     */
    suspend fun insert(content: String): Boolean

    /**
     * Coroutine에서 delete 수행
     *
     * @param number 지울 Log의 고유 번호
     * @return 성공 여부
     */
    suspend fun delete(number: Int): Boolean

    /**
     * Coroutine에서 update 수행
     *
     * @param number 수정할 Log의 고유 번호
     * @param newContent 수정할 Log 내용
     * @return 성공 여부
     */
    suspend fun modify(number: Int, newContent: String): Boolean

    /**
     * Coroutine에서 전체 행 가져오는 select 수행
     *
     * @return 데이터베이스에 저장된 전체 Log 리스트
     */
    suspend fun getAllLog(): List<LogItem>
}

/**
 * SQLite를 간편하게 쓸 수 있게 해주는 어댑터 클래스
 */
class SqliteManager private constructor(context: Context) : DatabaseManager {
    // SqliteOpenHelper 구현 클래스
    private val dbHelper = SqliteHelper(context)
    // 읽고 쓸 수 있는 SQLiteDatabase 인스턴스
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
                // 컬럼 이름을 넘겨서 인덱스 번호를 획득하고, 인덱스 번호로 컬럼 값 가져옴
                val number = it.getInt(it.getColumnIndexOrThrow(LogTable.COLUMN_NAME_NUMBER))
                val content = it.getString(it.getColumnIndexOrThrow(LogTable.COLUMN_NAME_CONTENT))

                logs.add(LogItem(number, content))
            }
        }

        logs
    }

    /**
     * query 에러 시 예외 잡기 위한 try-catch
     */
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

/**
 * Room을 간편하게 쓸 수 있게 해주는 어댑터 클래스
 */
class RoomManager private constructor(context: Context) : DatabaseManager {
    // RoomDatabase의 구현 클래스를 넘기면서 database 인스턴스 생성
    private val database = Room.databaseBuilder(context, LogDatabase::class.java, DatabaseInfo.DATABASE_NAME_ROOM).build()

    companion object {
        private var roomManager: RoomManager? = null
        fun getInstance(context: Context) = roomManager ?: RoomManager(context)
    }

    override suspend fun insert(content: String) = tryExecute {
        val newItem = LogItem(0, content)
        database.logItemDao().insert(newItem)
    }

    override suspend fun delete(number: Int) = tryExecute {
        val deleteItem = LogItem(number, "")
        database.logItemDao().delete(deleteItem)
    }

    override suspend fun modify(number: Int, newContent: String) = tryExecute {
        val modifyItem = LogItem(number, newContent)
        database.logItemDao().modify(modifyItem)
    }

    override suspend fun getAllLog(): List<LogItem> = database.logItemDao().getAll()

    /**
     * query 에러 시 예외 잡기 위한 try-catch
     */
    private suspend fun tryExecute(block: suspend () -> Unit): Boolean {
        return try {
            block()
            true
        } catch (e: SQLException) {
            e.printStackTrace()
            false
        }
    }
}