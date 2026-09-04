package com.example.srs.domain.model

/** A user-owned collection of notes and cards. A parent creates an optional deck hierarchy. */
data class Deck(
    val id: DeckId,
    val name: String,
    val parentId: DeckId? = null,
    val createdAt: Timestamp,
    val updatedAt: Timestamp,
    val isArchived: Boolean = false,
) {
    init {
        require(name.isNotBlank()) { "A deck must have a name." }
        require(parentId != id) { "A deck cannot be its own parent." }
    }
}
