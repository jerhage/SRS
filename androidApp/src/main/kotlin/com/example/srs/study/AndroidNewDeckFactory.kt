package com.example.srs.study

import com.example.srs.feature.study.NewDeckFactory
import com.example.srs.feature.study.model.Deck
import com.example.srs.feature.study.model.DeckId
import com.example.srs.feature.study.model.Timestamp
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
