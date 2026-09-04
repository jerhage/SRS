package com.example.srs.data.repository

import com.example.srs.data.database.Card
import com.example.srs.data.database.SrsDatabase
import com.example.srs.feature.study.model.Card as DomainCard
import com.example.srs.feature.study.model.CardId
import com.example.srs.feature.study.model.CardPhase
import com.example.srs.feature.study.model.DeckId
import com.example.srs.feature.study.model.NoteId
import com.example.srs.feature.study.model.SchedulingState
import com.example.srs.feature.study.model.Timestamp
import com.example.srs.feature.study.CardRepository

class SqliteCardRepository(
    database: SrsDatabase,
) : CardRepository {
    private val queries = database.cardQueries

    override suspend fun get(id: CardId): DomainCard? =
        queries.selectById(id.value).executeAsOneOrNull()?.toDomain()

    override suspend fun getByNote(noteId: NoteId): List<DomainCard> =
        queries.selectByNote(noteId.value).executeAsList().map(Card::toDomain)

    override suspend fun getDue(deckId: DeckId, at: Timestamp, limit: Int): List<DomainCard> {
        require(limit > 0) { "Due-card limit must be positive." }
        return queries.selectDue(deckId.value, at.epochMilliseconds, limit.toLong())
            .executeAsList()
            .map(Card::toDomain)
    }

    override suspend fun save(card: DomainCard) {
        queries.upsert(
            id = card.id.value,
            noteId = card.noteId.value,
            deckId = card.deckId.value,
            ordinal = card.ordinal.toLong(),
            prompt = card.prompt,
            answer = card.answer,
            phase = card.scheduling.phase.name,
            dueAt = card.scheduling.dueAt.epochMilliseconds,
            intervalDays = card.scheduling.intervalDays.toLong(),
            easeFactor = card.scheduling.easeFactor,
            repetitions = card.scheduling.repetitions.toLong(),
            lapses = card.scheduling.lapses.toLong(),
            isSuspended = if (card.isSuspended) 1 else 0,
            createdAt = card.createdAt.epochMilliseconds,
            updatedAt = card.updatedAt.epochMilliseconds,
        )
    }

    override suspend fun delete(id: CardId) {
        queries.deleteById(id.value)
    }
}

private fun Card.toDomain(): DomainCard = DomainCard(
    id = CardId(id),
    noteId = NoteId(note_id),
    deckId = DeckId(deck_id),
    ordinal = ordinal.toInt(),
    prompt = prompt,
    answer = answer,
    scheduling = SchedulingState(
        phase = CardPhase.valueOf(phase),
        dueAt = Timestamp(due_at),
        intervalDays = interval_days.toInt(),
        easeFactor = ease_factor,
        repetitions = repetitions.toInt(),
        lapses = lapses.toInt(),
    ),
    createdAt = Timestamp(created_at),
    updatedAt = Timestamp(updated_at),
    isSuspended = is_suspended != 0L,
)
