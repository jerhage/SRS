package com.example.srs.data

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.example.srs.data.db.DatabaseDriverFactory
import com.example.srs.data.db.SrsDatabase
import com.example.srs.domain.model.Deck
import com.example.srs.domain.model.DeckId
import com.example.srs.domain.model.Card
import com.example.srs.domain.model.CardId
import com.example.srs.domain.model.CardPhase
import com.example.srs.domain.model.NoteId
import com.example.srs.domain.model.Note
import com.example.srs.domain.model.SchedulingState
import com.example.srs.domain.model.Timestamp
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class SrsDataStoreTest {
    private lateinit var driver: JdbcSqliteDriver
    private lateinit var store: SrsDataStore

    @BeforeTest
    fun setUp() {
        driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY, schema = SrsDatabase.Schema)
        store = SrsDataStore(object : DatabaseDriverFactory {
            override fun createDriver() = driver
        })
    }

    @AfterTest
    fun tearDown() {
        driver.close()
    }

    @Test
    fun `persisted deck can be retrieved through the injected data store`() = runTest {
        val deck = deck(id = "languages", name = "Languages")

        store.decks.save(deck)

        assertEquals(deck, store.decks.get(deck.id))
    }

    @Test
    fun `archived decks are hidden unless explicitly requested`() = runTest {
        val active = deck(id = "active", name = "Active")
        val archived = deck(id = "archived", name = "Archived", isArchived = true)

        store.decks.save(active)
        store.decks.save(archived)

        assertEquals(listOf(active), store.decks.getAll())
        assertEquals(listOf(active, archived), store.decks.getAll(includeArchived = true))
    }

    @Test
    fun `deleted decks can no longer be retrieved`() = runTest {
        val deck = deck(id = "temporary", name = "Temporary")
        store.decks.save(deck)

        store.decks.delete(deck.id)

        assertNull(store.decks.get(deck.id))
    }

    @Test
    fun `notes preserve their fields and tags`() = runTest {
        val deck = deck(id = "languages", name = "Languages")
        val note = note(id = "spanish-basics", deckId = deck.id)
        store.decks.save(deck)

        store.notes.save(note)

        assertEquals(note, store.notes.get(note.id))
        assertEquals(listOf(note), store.notes.getByDeck(deck.id))
    }

    @Test
    fun `cards are persisted in their deck and returned when due`() = runTest {
        val deck = deck(id = "languages", name = "Languages")
        store.decks.save(deck)
        val note = note(id = "spanish-basics", deckId = deck.id)
        store.notes.save(note)
        val dueCard = card(id = "hola", deckId = deck.id, noteId = note.id, dueAt = 5_000)
        val futureCard = card(id = "adios", deckId = deck.id, noteId = note.id, dueAt = 15_000, ordinal = 1)

        store.cards.save(dueCard)
        store.cards.save(futureCard)

        assertEquals(dueCard, store.cards.get(dueCard.id))
        assertEquals(listOf(dueCard), store.cards.getDue(deck.id, Timestamp(10_000), limit = 10))
    }

    @Test
    fun `suspended cards are excluded from the due queue`() = runTest {
        val deck = deck(id = "science", name = "Science")
        store.decks.save(deck)
        val note = note(id = "atoms", deckId = deck.id)
        store.notes.save(note)
        val suspendedCard = card(id = "atom", deckId = deck.id, noteId = note.id, dueAt = 5_000, isSuspended = true)

        store.cards.save(suspendedCard)

        assertEquals(emptyList(), store.cards.getDue(deck.id, Timestamp(10_000), limit = 10))
    }

    private fun deck(id: String, name: String, isArchived: Boolean = false) = Deck(
        id = DeckId(id),
        name = name,
        createdAt = Timestamp(1_000),
        updatedAt = Timestamp(2_000),
        isArchived = isArchived,
    )

    private fun note(id: String, deckId: DeckId) = Note(
        id = NoteId(id),
        deckId = deckId,
        fields = mapOf("front" to "Question", "back" to "Answer"),
        tags = setOf("test", "basic"),
        createdAt = Timestamp(1_000),
        updatedAt = Timestamp(2_000),
    )

    private fun card(
        id: String,
        deckId: DeckId,
        noteId: NoteId,
        dueAt: Long,
        ordinal: Int = 0,
        isSuspended: Boolean = false,
    ) = Card(
        id = CardId(id),
        noteId = noteId,
        deckId = deckId,
        ordinal = ordinal,
        prompt = "Prompt $id",
        answer = "Answer $id",
        scheduling = SchedulingState(CardPhase.NEW, Timestamp(dueAt)),
        createdAt = Timestamp(1_000),
        updatedAt = Timestamp(2_000),
        isSuspended = isSuspended,
    )
}
