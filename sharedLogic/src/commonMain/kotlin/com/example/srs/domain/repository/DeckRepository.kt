package com.example.srs.domain.repository

import com.example.srs.domain.model.Deck
import com.example.srs.domain.model.DeckId

/** Product-facing access to decks. Implementations decide how decks are stored. */
interface DeckRepository {
    suspend fun get(id: DeckId): Deck?

    suspend fun getAll(includeArchived: Boolean = false): List<Deck>

    suspend fun save(deck: Deck)

    suspend fun delete(id: DeckId)
}
