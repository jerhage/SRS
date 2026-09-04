package com.example.srs.data

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlinx.coroutines.test.runTest

class DeckManagementScenariosTest : InMemorySrsDataStoreTest() {
    @Test
    fun `user creates a deck and can open it later`() = runTest {
        val deck = deck(id = "languages", name = "Languages")

        store.decks.save(deck)

        assertEquals(deck, store.decks.get(deck.id))
    }

    @Test
    fun `user organizes a deck beneath a parent deck`() = runTest {
        val parent = deck(id = "languages", name = "Languages")
        val child = deck(id = "languages-spanish", name = "Spanish", parentId = parent.id)
        store.decks.save(parent)

        store.decks.save(child)

        assertEquals(child, store.decks.get(child.id))
    }

    @Test
    fun `user hides an archived deck from their active deck list`() = runTest {
        val active = deck(id = "active", name = "Active")
        val archived = deck(id = "archived", name = "Archived", isArchived = true)
        store.decks.save(active)
        store.decks.save(archived)

        assertEquals(listOf(active), store.decks.getAll())
        assertEquals(listOf(active, archived), store.decks.getAll(includeArchived = true))
    }

    @Test
    fun `user deletes a deck`() = runTest {
        val deck = deck(id = "temporary", name = "Temporary")
        store.decks.save(deck)

        store.decks.delete(deck.id)

        assertNull(store.decks.get(deck.id))
    }
}
