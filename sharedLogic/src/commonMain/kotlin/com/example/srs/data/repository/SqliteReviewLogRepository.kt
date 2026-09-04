package com.example.srs.data.repository

import com.example.srs.data.database.SrsDatabase
import com.example.srs.feature.study.model.CardId
import com.example.srs.feature.study.model.CardPhase
import com.example.srs.feature.study.model.ReviewLog as DomainReviewLog
import com.example.srs.feature.study.model.ReviewLogId
import com.example.srs.feature.study.model.ReviewRating
import com.example.srs.feature.study.model.SchedulingState
import com.example.srs.feature.study.model.Timestamp
import com.example.srs.feature.study.ReviewLogRepository

class SqliteReviewLogRepository(
    database: SrsDatabase,
) : ReviewLogRepository {
    private val queries = database.reviewLogQueries

    // using this instead of a .toDomain cause it presents an alternative solution.
    override suspend fun getByCard(cardId: CardId): List<DomainReviewLog> =
        queries.selectByCard(cardId.value) {
                id,
                storedCardId,
                reviewedAt,
                rating,
                elapsedMilliseconds,
                previousPhase,
                previousDueAt,
                previousIntervalDays,
                previousEaseFactor,
                previousRepetitions,
                previousLapses,
            ->
            DomainReviewLog(
                id = ReviewLogId(id),
                cardId = CardId(storedCardId),
                reviewedAt = Timestamp(reviewedAt),
                rating = ReviewRating.valueOf(rating),
                elapsedMilliseconds = elapsedMilliseconds,
                previousScheduling = SchedulingState(
                    phase = CardPhase.valueOf(previousPhase),
                    dueAt = Timestamp(previousDueAt),
                    intervalDays = previousIntervalDays.toInt(),
                    easeFactor = previousEaseFactor,
                    repetitions = previousRepetitions.toInt(),
                    lapses = previousLapses.toInt(),
                ),
            )
        }
            .executeAsList()

    override suspend fun append(log: DomainReviewLog) {
        queries.insert(
            id = log.id.value,
            cardId = log.cardId.value,
            reviewedAt = log.reviewedAt.epochMilliseconds,
            rating = log.rating.name,
            elapsedMilliseconds = log.elapsedMilliseconds,
            previousPhase = log.previousScheduling.phase.name,
            previousDueAt = log.previousScheduling.dueAt.epochMilliseconds,
            previousIntervalDays = log.previousScheduling.intervalDays.toLong(),
            previousEaseFactor = log.previousScheduling.easeFactor,
            previousRepetitions = log.previousScheduling.repetitions.toLong(),
            previousLapses = log.previousScheduling.lapses.toLong(),
        )
    }
}
