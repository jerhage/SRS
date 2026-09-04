package com.example.srs.domain.model

import kotlin.test.Test
import kotlin.test.assertFailsWith

class DomainModelTest {
    @Test
    fun `a card rejects a blank prompt`() {
        assertFailsWith<IllegalArgumentException> {
            Card(
                id = CardId("card-1"),
                noteId = NoteId("note-1"),
                deckId = DeckId("deck-1"),
                ordinal = 0,
                prompt = " ",
                answer = "Answer",
                scheduling = SchedulingState(CardPhase.NEW, Timestamp(0)),
                createdAt = Timestamp(0),
                updatedAt = Timestamp(0),
            )
        }
    }

    @Test
    fun `scheduling state rejects a negative interval`() {
        assertFailsWith<IllegalArgumentException> {
            SchedulingState(CardPhase.REVIEW, Timestamp(0), intervalDays = -1)
        }
    }
}
