package com.example.srs.feature.study

import com.example.srs.feature.study.model.Timestamp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest

class StudyQueueScenariosTest : InMemorySrsDataStoreTest() {
    @Test
    fun `user sees cards from their deck when they are due`() = runTest {
        val deck = deck(id = "languages", name = "Languages")
        val note = note(id = "spanish-basics", deckId = deck.id)
        val dueCard = card(id = "hola", deckId = deck.id, noteId = note.id, dueAt = 5_000)
        val futureCard = card(id = "adios", deckId = deck.id, noteId = note.id, dueAt = 15_000, ordinal = 1)
        store.decks.save(deck)
        store.notes.save(note)

        store.cards.save(dueCard)
        store.cards.save(futureCard)

        assertEquals(dueCard, store.cards.get(dueCard.id))
        assertEquals(listOf(dueCard), store.cards.getDue(deck.id, Timestamp(10_000), limit = 10))
    }

    @Test
    fun `user does not see a suspended card in their study queue`() = runTest {
        val deck = deck(id = "science", name = "Science")
        val note = note(id = "atoms", deckId = deck.id)
        val suspendedCard = card(
            id = "atom",
            deckId = deck.id,
            noteId = note.id,
            dueAt = 5_000,
            isSuspended = true,
        )
        store.decks.save(deck)
        store.notes.save(note)

        store.cards.save(suspendedCard)

        assertEquals(emptyList(), store.cards.getDue(deck.id, Timestamp(10_000), limit = 10))
    }
}
