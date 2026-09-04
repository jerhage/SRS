package com.example.srs.feature.study

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.example.srs.data.SrsDataStore
import com.example.srs.data.database.DatabaseDriverFactory
import com.example.srs.data.database.SrsDatabase
import com.example.srs.feature.study.model.Card
import com.example.srs.feature.study.model.CardId
import com.example.srs.feature.study.model.CardPhase
import com.example.srs.feature.study.model.Deck
import com.example.srs.feature.study.model.DeckId
import com.example.srs.feature.study.model.Note
import com.example.srs.feature.study.model.NoteId
import com.example.srs.feature.study.model.SchedulingState
import com.example.srs.feature.study.model.Timestamp
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

abstract class InMemorySrsDataStoreTest {
    private lateinit var driver: JdbcSqliteDriver

    protected lateinit var store: SrsDataStore

    @BeforeTest
    fun setUpDataStore() {
        driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY, schema = SrsDatabase.Schema)
        store = SrsDataStore(object : DatabaseDriverFactory {
            override fun createDriver() = driver
        })
    }

    @AfterTest
    fun tearDownDataStore() {
        driver.close()
    }

    protected fun deck(
        id: String,
        name: String,
        parentId: DeckId? = null,
        isArchived: Boolean = false,
    ) = Deck(
        id = DeckId(id),
        name = name,
        parentId = parentId,
        createdAt = Timestamp(1_000),
        updatedAt = Timestamp(2_000),
        isArchived = isArchived,
    )

    protected fun note(
        id: String,
        deckId: DeckId,
        fields: Map<String, String> = mapOf("front" to "Question", "back" to "Answer"),
        tags: Set<String> = setOf("test", "basic"),
    ) = Note(
        id = NoteId(id),
        deckId = deckId,
        fields = fields,
        tags = tags,
        createdAt = Timestamp(1_000),
        updatedAt = Timestamp(2_000),
    )

    protected fun card(
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
