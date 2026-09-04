package com.example.srs.feature.study.card

import com.example.srs.feature.study.deck.DeckId
import com.example.srs.feature.study.note.NoteId
import com.example.srs.feature.study.time.Timestamp
import kotlin.jvm.JvmInline

/** A single reviewable prompt. Multiple cards may be produced from one note. */
data class Card(
    val id: CardId,
    val noteId: NoteId,
    val deckId: DeckId,
    val ordinal: Int,
    val prompt: String,
    val answer: String,
    val scheduling: SchedulingState,
    val createdAt: Timestamp,
    val updatedAt: Timestamp,
    val isSuspended: Boolean = false,
) {
    init {
        require(ordinal >= 0) { "Card ordinal cannot be negative." }
        require(prompt.isNotBlank()) { "A card must have a prompt." }
        require(answer.isNotBlank()) { "A card must have an answer." }
    }
}

@JvmInline
value class CardId(val value: String)
