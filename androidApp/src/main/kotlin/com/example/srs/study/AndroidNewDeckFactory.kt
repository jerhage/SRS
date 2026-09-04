package com.example.srs.study

import com.example.srs.feature.study.deck.Deck
import com.example.srs.feature.study.deck.DeckId
import com.example.srs.feature.study.deck.NewDeckFactory
import com.example.srs.feature.study.time.Timestamp
import java.util.UUID

class AndroidNewDeckFactory : NewDeckFactory {
    override fun create(name: String): Deck {
        val now = Timestamp(System.currentTimeMillis())
        return Deck(
            id = DeckId(UUID.randomUUID().toString()),
            name = name,
            createdAt = now,
            updatedAt = now,
        )
    }
}
