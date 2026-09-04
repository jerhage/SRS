package com.example.srs.feature.study.card

import com.example.srs.feature.study.deck.DeckId
import com.example.srs.feature.study.note.NoteId
import com.example.srs.feature.study.time.Timestamp

interface CardRepository {
    suspend fun get(id: CardId): Card?

    suspend fun getByNote(noteId: NoteId): List<Card>

    suspend fun getDue(deckId: DeckId, at: Timestamp, limit: Int): List<Card>

    suspend fun save(card: Card)

    suspend fun delete(id: CardId)
}
