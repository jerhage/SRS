package com.example.srs.feature.study

import com.example.srs.feature.study.model.Timestamp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlinx.coroutines.test.runTest

class NoteManagementScenariosTest : InMemorySrsDataStoreTest() {
    @Test
    fun `user creates a note in a deck with fields and tags`() = runTest {
        val deck = deck(id = "languages", name = "Languages")
        val note = note(id = "spanish-basics", deckId = deck.id)
        store.decks.save(deck)

        store.notes.save(note)

        assertEquals(note, store.notes.get(note.id))
        assertEquals(listOf(note), store.notes.getByDeck(deck.id))
    }

    @Test
    fun `user edits a note's fields and tags`() = runTest {
        val deck = deck(id = "languages", name = "Languages")
        val original = note(id = "spanish-basics", deckId = deck.id)
        val edited = original.copy(
            fields = mapOf("front" to "Hola", "back" to "Hello"),
            tags = setOf("spanish", "greetings"),
            updatedAt = Timestamp(3_000),
        )
        store.decks.save(deck)
        store.notes.save(original)

        store.notes.save(edited)

        assertEquals(edited, store.notes.get(original.id))
    }

    @Test
    fun `user deletes a note from a deck`() = runTest {
        val deck = deck(id = "languages", name = "Languages")
        val note = note(id = "spanish-basics", deckId = deck.id)
        store.decks.save(deck)
        store.notes.save(note)

        store.notes.delete(note.id)

        assertNull(store.notes.get(note.id))
        assertEquals(emptyList(), store.notes.getByDeck(deck.id))
    }
}
