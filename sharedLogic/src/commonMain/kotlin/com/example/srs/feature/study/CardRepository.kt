package com.example.srs.feature.study

import com.example.srs.feature.study.model.Card
import com.example.srs.feature.study.model.CardId
import com.example.srs.feature.study.model.DeckId
import com.example.srs.feature.study.model.NoteId
import com.example.srs.feature.study.model.Timestamp

interface CardRepository {
    suspend fun get(id: CardId): Card?

    suspend fun getByNote(noteId: NoteId): List<Card>

    suspend fun getDue(deckId: DeckId, at: Timestamp, limit: Int): List<Card>

    suspend fun save(card: Card)

    suspend fun delete(id: CardId)
}
