package com.example.srs.domain.model

/** The durable scheduling facts for a card; scheduling policy belongs in a scheduler later. */
data class SchedulingState(
    val phase: CardPhase,
    val dueAt: Timestamp,
    val intervalDays: Int = 0,
    val easeFactor: Double = INITIAL_EASE_FACTOR,
    val repetitions: Int = 0,
    val lapses: Int = 0,
) {
    init {
        require(intervalDays >= 0) { "Interval cannot be negative." }
        require(easeFactor >= MINIMUM_EASE_FACTOR) { "Ease factor is below its minimum." }
        require(repetitions >= 0) { "Repetitions cannot be negative." }
        require(lapses >= 0) { "Lapses cannot be negative." }
    }

    companion object {
        const val INITIAL_EASE_FACTOR = 2.5
        const val MINIMUM_EASE_FACTOR = 1.3
    }
}

enum class CardPhase {
    NEW,
    LEARNING,
    REVIEW,
    RELEARNING,
}
