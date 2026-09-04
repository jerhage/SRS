package com.example.srs.data.db

class SrsDatabaseFactory(
    private val driverFactory: DatabaseDriverFactory,
) {
    fun create(): SrsDatabase = SrsDatabase(driverFactory.createDriver())
}
