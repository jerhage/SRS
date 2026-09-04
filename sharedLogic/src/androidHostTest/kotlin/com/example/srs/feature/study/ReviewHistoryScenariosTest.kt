package com.example.srs.feature.study

import com.example.srs.feature.study.model.CardPhase
import com.example.srs.feature.study.model.ReviewLog
import com.example.srs.feature.study.model.ReviewLogId
import com.example.srs.feature.study.model.ReviewRating
import com.example.srs.feature.study.model.SchedulingState
import com.example.srs.feature.study.model.Timestamp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest

class ReviewHistoryScenariosTest : InMemorySrsDataStoreTest() {
    @Test
    fun `user's completed review retains its scheduling snapshot`() = runTest {
        val deck = deck(id = "languages", name = "Languages")
        val note = note(id = "spanish-basics", deckId = deck.id)
        val card = card(id = "hola", deckId = deck.id, noteId = note.id, dueAt = 5_000)
        val reviewLog = ReviewLog(
            id = ReviewLogId("review-1"),
            cardId = card.id,
            reviewedAt = Timestamp(10_000),
            rating = ReviewRating.HARD,
            elapsedMilliseconds = 3_200,
            previousScheduling = SchedulingState(
                phase = CardPhase.REVIEW,
                dueAt = Timestamp(5_000),
                intervalDays = 21,
                easeFactor = 2.35,
                repetitions = 8,
                lapses = 2,
            ),
        )
        store.decks.save(deck)
        store.notes.save(note)
        store.cards.save(card)

        store.reviewLogs.append(reviewLog)

        assertEquals(listOf(reviewLog), store.reviewLogs.getByCard(card.id))
    }
}
