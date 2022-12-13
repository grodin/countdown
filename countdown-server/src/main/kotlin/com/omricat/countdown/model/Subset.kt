package com.omricat.countdown.model

fun LetterMultiset.isSubsetOf(other: LetterMultiset): Boolean =
  this.hash.divides(other.hash)

fun LetterMultiset.isSupersetOf(other: LetterMultiset): Boolean =
  other.hash.divides(this.hash)

@Suppress("NOTHING_TO_INLINE")
inline fun Long.divides(other: Long): Boolean = other % this == 0L
