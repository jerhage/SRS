package com.example.srs.feature.study.review

import com.example.srs.feature.study.card.CardId
import com.example.srs.feature.study.card.SchedulingState
import com.example.srs.feature.study.time.Timestamp
import kotlin.jvm.JvmInline

/** An immutable record of a completed answer and the state that preceded it. */
data class ReviewLog(
    val id: ReviewLogId,
    val cardId: CardId,
    val reviewedAt: Timestamp,
    val rating: ReviewRating,
    val elapsedMilliseconds: Long,
    val previousScheduling: SchedulingState,
) {
    init {
        require(elapsedMilliseconds >= 0) { "Review time cannot be negative." }
    }
}

@JvmInline
value class ReviewLogId(val value: String)

/** The user-facing answer quality; the scheduler translates it into the next state. */
enum class ReviewRating {
    AGAIN,
    HARD,
    GOOD,
    EASY,
}
