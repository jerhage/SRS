package com.example.srs.feature.study

import com.example.srs.feature.study.model.Deck
import com.example.srs.feature.study.model.DeckId

/** Product-facing access to decks. Implementations decide how decks are stored. */
interface DeckRepository {
    suspend fun get(id: DeckId): Deck?

    suspend fun getAll(includeArchived: Boolean = false): List<Deck>

    suspend fun save(deck: Deck)

    suspend fun delete(id: DeckId)
}
