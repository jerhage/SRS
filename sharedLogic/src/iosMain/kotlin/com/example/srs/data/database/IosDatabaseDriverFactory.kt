package com.example.srs.data.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver

class IosDatabaseDriverFactory : DatabaseDriverFactory {
    override fun createDriver(): SqlDriver = NativeSqliteDriver(
        schema = SrsDatabase.Schema,
        name = DATABASE_NAME,
    )

    private companion object {
        const val DATABASE_NAME = "srs.db"
    }
}
