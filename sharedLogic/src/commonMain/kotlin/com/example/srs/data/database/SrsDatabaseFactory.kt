package com.example.srs.data.database

class SrsDatabaseFactory(
    private val driverFactory: DatabaseDriverFactory,
) {
    fun create(): SrsDatabase = SrsDatabase(driverFactory.createDriver())
}
