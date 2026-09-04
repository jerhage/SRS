package com.example.srs.data.repository

import com.example.srs.data.database.Deck
import com.example.srs.data.database.SrsDatabase
import com.example.srs.feature.study.deck.Deck as DomainDeck
import com.example.srs.feature.study.deck.DeckId
import com.example.srs.feature.study.deck.DeckRepository
import com.example.srs.feature.study.time.Timestamp

class SqliteDeckRepository(
    database: SrsDatabase,
) : DeckRepository {
    private val queries = database.deckQueries

    override suspend fun get(id: DeckId): DomainDeck? =
        queries.selectById(id.value).executeAsOneOrNull()?.toDomain()

    override suspend fun getAll(includeArchived: Boolean): List<DomainDeck> =
        queries.selectAll(if (includeArchived) 1 else 0).executeAsList().map(Deck::toDomain)

    override suspend fun save(deck: DomainDeck) {
        queries.upsert(
            id = deck.id.value,
            name = deck.name,
            parentId = deck.parentId?.value,
            createdAt = deck.createdAt.epochMilliseconds,
            updatedAt = deck.updatedAt.epochMilliseconds,
            isArchived = if (deck.isArchived) 1 else 0,
        )
    }

    override suspend fun delete(id: DeckId) {
        queries.deleteById(id.value)
    }
}

private fun Deck.toDomain(): DomainDeck = DomainDeck(
    id = DeckId(id),
    name = name,
    parentId = parent_id?.let(::DeckId),
    createdAt = Timestamp(created_at),
    updatedAt = Timestamp(updated_at),
    isArchived = is_archived != 0L,
)
