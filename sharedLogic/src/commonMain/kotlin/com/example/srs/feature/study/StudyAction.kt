package com.example.srs.feature.study

sealed interface StudyAction {
    data class DeckNameChanged(val name: String) : StudyAction

    data object CreateDeck : StudyAction

    data object Refresh : StudyAction
}
