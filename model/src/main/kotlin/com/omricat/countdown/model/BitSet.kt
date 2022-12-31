package com.omricat.countdown.model

import kotlin.streams.toList

/**
 * Wrapper around [java.util.BitSet] to expose only the API we need
 */
@JvmInline
internal value class BitSet private constructor(private val bits: java.util.BitSet) {
  constructor(numBits: Int) : this(
    java.util.BitSet().apply { set(0, numBits) }
  )

  fun indicesOfSetBits(): List<Int> = bits.stream().toList()

  companion object {

    fun allOfWidth(width: Int): Sequence<BitSet> =
      (0 until 1.shl(width))
        .asSequence()
        .map { BitSet(java.util.BitSet.valueOf(longArrayOf(it.toLong()))) }
  }
}

/**
 * If we think of a List<Pair<E, UShort>> as a compressed version of a sorted
 * list, this function computes the compressed, sorted versions of all sublists
 * of the original uncompressed list.
 *
 * E.g. [('a', 3),('x',2'),('b',1)] represents \[aaaxxb\] and the sublists would
 * be [], [a], [aa], [aaa], [ax], [aax],...,[axx], [aaxx],...,[ab], etc.
 * which would be represented as [], [('a',1)],...,[('a',1),('x',1)], etc.
 */
internal fun <E> List<Pair<E, UShort>>.subListsByBitSet(bitSet: BitSet):
    List<List<Pair<E, UShort>>> =
  bitSet.indicesOfSetBits()
    .map { idx -> this[idx].first to this[idx].second }
    .fold(listOf(listOf())) { lists, (element, count) ->
      lists.flatMap { subList: List<Pair<E, UShort>> ->
        (1..count.toInt()).map { subList + (element to it.toUShort()) }
      }
    }
