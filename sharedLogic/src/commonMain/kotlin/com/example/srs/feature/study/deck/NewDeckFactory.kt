package com.example.srs.feature.study.deck

/** Creates a new deck with platform-provided identity and timestamps. */
fun interface NewDeckFactory {
    fun create(name: String): Deck
}
