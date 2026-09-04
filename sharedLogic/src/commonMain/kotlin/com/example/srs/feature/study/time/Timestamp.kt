package com.example.srs.feature.study.time

import kotlin.jvm.JvmInline

/** An instant represented in UTC milliseconds since the Unix epoch. */
@JvmInline
value class Timestamp(val epochMilliseconds: Long) : Comparable<Timestamp> {
    override fun compareTo(other: Timestamp): Int = epochMilliseconds.compareTo(other.epochMilliseconds)
}
