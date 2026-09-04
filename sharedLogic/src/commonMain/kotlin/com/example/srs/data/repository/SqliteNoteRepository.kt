package com.example.srs.data.repository

import com.example.srs.data.db.Note
import com.example.srs.data.db.SrsDatabase
import com.example.srs.domain.model.DeckId
import com.example.srs.domain.model.Note as DomainNote
import com.example.srs.domain.model.NoteId
import com.example.srs.domain.model.Timestamp
import com.example.srs.domain.repository.NoteRepository
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.SetSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

class SqliteNoteRepository(
    database: SrsDatabase,
    private val json: Json = Json,
) : NoteRepository {
    private val queries = database.noteQueries

    override suspend fun get(id: NoteId): DomainNote? =
        queries.selectById(id.value).executeAsOneOrNull()?.toDomain(json)

    override suspend fun getByDeck(deckId: DeckId, includeArchived: Boolean): List<DomainNote> =
        queries.selectByDeck(deckId.value, if (includeArchived) 1 else 0)
            .executeAsList()
            .map { it.toDomain(json) }

    override suspend fun save(note: DomainNote) {
        queries.upsert(
            id = note.id.value,
            deckId = note.deckId.value,
            fieldsJson = json.encodeToString(fieldsSerializer, note.fields),
            tagsJson = json.encodeToString(tagsSerializer, note.tags),
            createdAt = note.createdAt.epochMilliseconds,
            updatedAt = note.updatedAt.epochMilliseconds,
            isArchived = if (note.isArchived) 1 else 0,
        )
    }

    override suspend fun delete(id: NoteId) {
        queries.deleteById(id.value)
    }
}

private val fieldsSerializer = MapSerializer(String.serializer(), String.serializer())
private val tagsSerializer = SetSerializer(String.serializer())

private fun Note.toDomain(json: Json): DomainNote = DomainNote(
    id = NoteId(id),
    deckId = DeckId(deck_id),
    fields = json.decodeFromString(fieldsSerializer, fields_json),
    tags = json.decodeFromString(tagsSerializer, tags_json),
    createdAt = Timestamp(created_at),
    updatedAt = Timestamp(updated_at),
    isArchived = is_archived != 0L,
)
