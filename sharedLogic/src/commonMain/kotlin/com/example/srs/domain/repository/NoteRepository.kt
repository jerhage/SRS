package com.example.srs.domain.repository

import com.example.srs.domain.model.DeckId
import com.example.srs.domain.model.Note
import com.example.srs.domain.model.NoteId

interface NoteRepository {
    suspend fun get(id: NoteId): Note?

    suspend fun getByDeck(deckId: DeckId, includeArchived: Boolean = false): List<Note>

    suspend fun save(note: Note)

    suspend fun delete(id: NoteId)
}
