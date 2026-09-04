package com.example.srs.feature.study

import com.example.srs.feature.study.model.Deck

/** Creates a new deck with platform-provided identity and timestamps. */
fun interface NewDeckFactory {
    fun create(name: String): Deck
}
