package com.example.srs.feature.study

import com.example.srs.feature.study.deck.Deck
import com.example.srs.feature.study.deck.DeckId
import com.example.srs.feature.study.deck.DeckRepository
import com.example.srs.feature.study.deck.NewDeckFactory
import com.example.srs.feature.study.time.Timestamp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlinx.coroutines.test.runTest

class StudyViewModelTest {
    @Test
    fun `loads active decks into study state`() = runTest {
        val decks = listOf(
            Deck(
                id = DeckId("spanish"),
                name = "Spanish",
                createdAt = Timestamp(1_000),
                updatedAt = Timestamp(1_000),
            ),
        )
        val viewModel = StudyViewModel(
            deckRepository = FakeDeckRepository(decks),
            newDeckFactory = FakeNewDeckFactory,
        )

        viewModel.refresh()

        assertEquals(decks, viewModel.state.value.decks)
        assertFalse(viewModel.state.value.isLoading)
        viewModel.close()
    }

    @Test
    fun `creates a named deck and clears the input`() = runTest {
        val repository = FakeDeckRepository()
        val viewModel = StudyViewModel(repository, FakeNewDeckFactory)
        viewModel.onAction(StudyAction.DeckNameChanged("Spanish"))

        viewModel.createDeck()

        assertEquals(listOf("Spanish"), viewModel.state.value.decks.map { it.name })
        assertEquals("", viewModel.state.value.deckName)
        viewModel.close()
    }
}

private class FakeDeckRepository(
    decks: List<Deck> = emptyList(),
) : DeckRepository {
    private val decks = decks.toMutableList()

    override suspend fun get(id: DeckId): Deck? = decks.firstOrNull { it.id == id }

    override suspend fun getAll(includeArchived: Boolean): List<Deck> = decks

    override suspend fun save(deck: Deck) {
        decks.removeAll { it.id == deck.id }
        decks += deck
    }

    override suspend fun delete(id: DeckId) = Unit
}

private object FakeNewDeckFactory : NewDeckFactory {
    override fun create(name: String): Deck = Deck(
        id = DeckId(name.lowercase()),
        name = name,
        createdAt = Timestamp(1_000),
        updatedAt = Timestamp(1_000),
    )
}
