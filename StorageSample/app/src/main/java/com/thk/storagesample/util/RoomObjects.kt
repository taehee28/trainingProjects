package com.thk.storagesample.util

import android.database.SQLException
import androidx.room.*
import com.thk.storagesample.model.LogItem

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

@Database(entities = [LogItem::class], version = DatabaseInfo.DATABASE_VERSION)
abstract class LogDatabase : RoomDatabase() {
    abstract fun logItemDao(): LogItemDao
}

