package com.example.srs.data.database

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

class AndroidDatabaseDriverFactory(
    private val context: Context,
) : DatabaseDriverFactory {
    override fun createDriver(): SqlDriver = AndroidSqliteDriver(
        schema = SrsDatabase.Schema,
        context = context,
        name = DATABASE_NAME,
        callback = object : AndroidSqliteDriver.Callback(SrsDatabase.Schema) {
            override fun onConfigure(db: SupportSQLiteDatabase) {
                db.execSQL("PRAGMA journal_mode = WAL")
                db.execSQL("PRAGMA synchronous = NORMAL")
                db.setForeignKeyConstraintsEnabled(true)
                db.execSQL("PRAGMA busy_timeout = 5000")
            }
        },
    )

    private companion object {
        const val DATABASE_NAME = "srs.db"
    }
}
