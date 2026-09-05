package com.example.srs.data.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import co.touchlab.sqliter.JournalMode
import co.touchlab.sqliter.SynchronousFlag

class IosDatabaseDriverFactory : DatabaseDriverFactory {
    override fun createDriver(): SqlDriver = NativeSqliteDriver(
        schema = SrsDatabase.Schema,
        name = DATABASE_NAME,
        onConfiguration = { config ->
            config.copy(
                journalMode = JournalMode.WAL,
                extendedConfig = config.extendedConfig.copy(
                    foreignKeyConstraints = true,
                    busyTimeout = 5_000,
                    synchronousFlag = SynchronousFlag.NORMAL,
                ),
            )
        },
    )

    private companion object {
        const val DATABASE_NAME = "srs.db"
    }
}
