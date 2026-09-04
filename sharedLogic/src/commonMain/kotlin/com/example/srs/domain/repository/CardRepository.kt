package com.example.srs.domain.repository

import com.example.srs.domain.model.Card
import com.example.srs.domain.model.CardId
import com.example.srs.domain.model.DeckId
import com.example.srs.domain.model.NoteId
import com.example.srs.domain.model.Timestamp

interface CardRepository {
    suspend fun get(id: CardId): Card?

    suspend fun getByNote(noteId: NoteId): List<Card>

    suspend fun getDue(deckId: DeckId, at: Timestamp, limit: Int): List<Card>

    suspend fun save(card: Card)

    suspend fun delete(id: CardId)
}
