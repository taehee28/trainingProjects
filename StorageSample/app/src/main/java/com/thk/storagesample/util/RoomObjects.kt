package com.thk.storagesample.util

import android.database.SQLException
import androidx.room.*
import com.thk.storagesample.model.LogItem

/**
 * 데이터 접근 객체
 */
@Dao
interface LogItemDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    @Throws(SQLException::class)
    suspend fun insert(logItem: LogItem)

    @Delete
    @Throws(SQLException::class)
    suspend fun delete(logItem: LogItem)

    @Update
    @Throws(SQLException::class)
    suspend fun modify(logItem: LogItem)

    @Query("select * from LogItem")
    suspend fun getAll(): List<LogItem>
}

/**
 * 데이터베이스 클래스
 */
@Database(entities = [LogItem::class], version = DatabaseInfo.DATABASE_VERSION)
abstract class LogDatabase : RoomDatabase() {
    // 데이터 접근 객체의 인스턴스를 반환하는 추상 메서드
    abstract fun logItemDao(): LogItemDao
}

