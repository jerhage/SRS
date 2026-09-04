package com.example.srs.data

import com.example.srs.data.database.DatabaseDriverFactory
import com.example.srs.data.database.SrsDatabaseFactory
import com.example.srs.data.repository.SqliteCardRepository
import com.example.srs.data.repository.SqliteDeckRepository
import com.example.srs.data.repository.SqliteNoteRepository
import com.example.srs.data.repository.SqliteReviewLogRepository
import com.example.srs.feature.study.card.CardRepository
import com.example.srs.feature.study.deck.DeckRepository
import com.example.srs.feature.study.note.NoteRepository
import com.example.srs.feature.study.review.ReviewLogRepository

class SrsDataStore(driverFactory: DatabaseDriverFactory) {
    private val database = SrsDatabaseFactory(driverFactory).create()

    val decks: DeckRepository = SqliteDeckRepository(database)
    val notes: NoteRepository = SqliteNoteRepository(database)
    val cards: CardRepository = SqliteCardRepository(database)
    val reviewLogs: ReviewLogRepository = SqliteReviewLogRepository(database)
}
