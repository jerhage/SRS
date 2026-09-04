package com.example.srs.feature.study.model

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

/** The user-facing answer quality; the scheduler translates it into the next state. */
enum class ReviewRating {
    AGAIN,
    HARD,
    GOOD,
    EASY,
}
