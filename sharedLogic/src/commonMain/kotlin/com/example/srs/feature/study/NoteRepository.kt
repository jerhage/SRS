package com.example.srs.feature.study

import com.example.srs.feature.study.model.DeckId
import com.example.srs.feature.study.model.Note
import com.example.srs.feature.study.model.NoteId

interface NoteRepository {
    suspend fun get(id: NoteId): Note?

    suspend fun getByDeck(deckId: DeckId, includeArchived: Boolean = false): List<Note>

    suspend fun save(note: Note)

    suspend fun delete(id: NoteId)
}
