package com.example.srs.domain.model

/**
 * The editable source material. Cards are the scheduled prompts generated from a note.
 *
 * Fields are deliberately named rather than positional so storage and future note types can
 * evolve without changing a card's review history.
 */
data class Note(
    val id: NoteId,
    val deckId: DeckId,
    val fields: Map<String, String>,
    val tags: Set<String> = emptySet(),
    val createdAt: Timestamp,
    val updatedAt: Timestamp,
    val isArchived: Boolean = false,
) {
    init {
        require(fields.isNotEmpty()) { "A note must contain at least one field." }
        require(fields.keys.all { it.isNotBlank() }) { "Note field names cannot be blank." }
        require(tags.none { it.isBlank() }) { "Tags cannot be blank." }
    }
}
