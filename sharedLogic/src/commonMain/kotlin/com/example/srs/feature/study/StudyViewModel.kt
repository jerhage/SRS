package com.example.srs.feature.study

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Shared presentation policy for the study home screen. */
class StudyViewModel(
    private val deckRepository: DeckRepository,
    private val newDeckFactory: NewDeckFactory,
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    private val scope = CoroutineScope(SupervisorJob() + dispatcher)
    private val mutableState = MutableStateFlow(StudyState())

    val state: StateFlow<StudyState> = mutableState.asStateFlow()

    fun onAction(action: StudyAction) {
        when (action) {
            is StudyAction.DeckNameChanged -> mutableState.update {
                it.copy(deckName = action.name, deckNameError = null)
            }
            StudyAction.CreateDeck -> scope.launch { createDeck() }
            StudyAction.Refresh -> scope.launch { refresh() }
        }
    }

    suspend fun refresh() {
        mutableState.update { it.copy(isLoading = true, errorMessage = null) }

        runCatching { deckRepository.getAll() }
            .onSuccess { decks ->
                mutableState.value = StudyState(decks = decks)
            }
            .onFailure {
                mutableState.update {
                    it.copy(isLoading = false, errorMessage = "Could not load decks.")
                }
            }
    }

    fun close() {
        scope.cancel()
    }

    suspend fun createDeck() {
        val name = mutableState.value.deckName.trim()
        if (name.isEmpty()) {
            mutableState.update { it.copy(deckNameError = "Enter a deck name.") }
            return
        }

        mutableState.update { it.copy(isCreatingDeck = true, deckNameError = null, errorMessage = null) }

        runCatching {
            deckRepository.save(newDeckFactory.create(name))
            deckRepository.getAll()
        }.onSuccess { decks ->
            mutableState.update {
                it.copy(decks = decks, deckName = "", isCreatingDeck = false)
            }
        }.onFailure {
            mutableState.update {
                it.copy(isCreatingDeck = false, errorMessage = "Could not create deck.")
            }
        }
    }
}
