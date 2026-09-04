package com.example.srs.data

import com.example.srs.data.db.DatabaseDriverFactory
import com.example.srs.data.db.SrsDatabaseFactory
import com.example.srs.data.repository.SqliteCardRepository
import com.example.srs.data.repository.SqliteDeckRepository
import com.example.srs.data.repository.SqliteNoteRepository
import com.example.srs.domain.repository.CardRepository
import com.example.srs.domain.repository.DeckRepository
import com.example.srs.domain.repository.NoteRepository

class SrsDataStore(driverFactory: DatabaseDriverFactory) {
    private val database = SrsDatabaseFactory(driverFactory).create()

    val decks: DeckRepository = SqliteDeckRepository(database)
    val notes: NoteRepository = SqliteNoteRepository(database)
    val cards: CardRepository = SqliteCardRepository(database)
}
