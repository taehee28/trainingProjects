package com.thk.storagesample.util

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * SQLiteOpenHelper를 구현하는 클래스
 */
class SqliteHelper(
    context: Context,
) : SQLiteOpenHelper(context, DatabaseInfo.DATABASE_NAME, null, DatabaseInfo.DATABASE_VERSION) {

    // 데이터베이스 생성 될 때 호출
    override fun onCreate(db: SQLiteDatabase?) {
        // 테이블이 없으면 생성하는 쿼리
        val query = "create table if not exists ${LogTable.TABLE_NAME} (" +
                "${LogTable.COLUMN_NAME_NUMBER} integer primary key autoincrement," +
                "${LogTable.COLUMN_NAME_CONTENT} varchar(35)" +
                ");"

        // 쿼리 실행
        db?.execSQL(query)
    }

    // 데이터베이스 버전 업그레이드 될 때 호출
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // 기존의 테이블 존재하면 삭제하는 쿼리
        val query = "drop table if exists ${LogTable.TABLE_NAME}"

        // 쿼리 실행
        db?.execSQL(query)
        // 데이터베이스 생성
        onCreate(db)
    }
}