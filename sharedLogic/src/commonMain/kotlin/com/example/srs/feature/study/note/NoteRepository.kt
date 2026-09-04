package com.example.srs.feature.study.note

import com.example.srs.feature.study.deck.DeckId

interface NoteRepository {
    suspend fun get(id: NoteId): Note?

    suspend fun getByDeck(deckId: DeckId, includeArchived: Boolean = false): List<Note>

    suspend fun save(note: Note)

    suspend fun delete(id: NoteId)
}
