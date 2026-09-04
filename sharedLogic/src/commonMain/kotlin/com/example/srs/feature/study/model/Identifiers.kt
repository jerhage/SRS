package com.example.srs.feature.study.model

import kotlin.jvm.JvmInline

/** Stable identifiers make relationships explicit without coupling the domain to a database. */
@JvmInline
value class DeckId(val value: String)

@JvmInline
value class NoteId(val value: String)

@JvmInline
value class CardId(val value: String)

@JvmInline
value class ReviewLogId(val value: String)

/** An instant represented in UTC milliseconds since the Unix epoch. */
@JvmInline
value class Timestamp(val epochMilliseconds: Long) : Comparable<Timestamp> {
    override fun compareTo(other: Timestamp): Int = epochMilliseconds.compareTo(other.epochMilliseconds)
}
