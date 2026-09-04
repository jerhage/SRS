package com.example.srs.feature.study

import com.example.srs.feature.study.model.Deck

data class StudyState(
    val decks: List<Deck> = emptyList(),
    val isLoading: Boolean = false,
    val deckName: String = "",
    val isCreatingDeck: Boolean = false,
    val deckNameError: String? = null,
    val errorMessage: String? = null,
)
