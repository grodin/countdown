package com.omricat.countdown.model

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.runCatching
import com.github.michaelbull.result.throwUnless
import com.github.michaelbull.result.fold as foldResult

/**
 * A class which represents the multiset of letters contained in an English word.
 */
public class LetterMultiset
private constructor(private val letters: List<Pair<Char, UShort>>) {

  private val mapOfLetters: Map<Char, UShort> by lazy {
    letters.associate { it }
  }

  public val hashResult: Result<Long, ArithmeticException> by lazy { computePrimePowerHash() }

  private fun computePrimePowerHash(): Result<Long, ArithmeticException> =
    mapOfLetters.mapKeys { (c, _) -> alphaToPrime.getValue(c) }
      .map { (prime, count) ->
        prime.power(count)
      }.product()

  private fun Prime.power(power: UShort): Long {
    var acc = 1L
    repeat(power.toInt()) {
      acc *= value
    }
    return acc
  }

  @Suppress("UNCHECKED_CAST")
  private fun Iterable<Long>.product(): Result<Long, ArithmeticException> =
    foldResult(1L) { acc, t ->
      runCatching {
        Math.multiplyExact(acc, t)
      }
    }.throwUnless { it is ArithmeticException }
        as Result<Long, ArithmeticException>

  /**
   * Computes the set of all sub-multisets of this [LetterMultiset].
   *
   * N.B. Iteration order of the returned set is not specified.
   */
  public fun subMultiSets(): Set<LetterMultiset> =
    BitSet.allOfWidth(letters.size)
      .flatMap { bitset ->
        letters.subListsByBitSet(bitset).map { LetterMultiset(it) }
      }.toSet()

  /**
   * Computes whether this [LetterMultiset] is a sub-multiset of the other.
   */
  public fun isSubsetOf(other: LetterMultiset): Boolean =
    mapOfLetters.all { (c, count) ->
      other.mapOfLetters[c]?.let { otherCount -> otherCount >= count } ?: false
    }

  /**
   * Computes whether this [LetterMultiset] is a super-multiset of the other.
   */
  public fun isSupersetOf(other: LetterMultiset): Boolean = other.isSubsetOf(this)

  public companion object {
    public fun fromWord(word: Word): LetterMultiset =
      word.value
        .sorted()
        .let { chars ->
          LetterMultiset(
            chars.groupBy { it }
              .mapValues { (_, list) -> list.size.toUShort() }.toList()
          )
        }
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as LetterMultiset

    if (letters != other.letters) return false

    return true
  }

  override fun hashCode(): Int {
    return letters.hashCode()
  }

  override fun toString(): String {
    val letterString =
      letters.joinToString(separator = "") { (c, count) ->
        "$c".repeat(count.toInt())
      }
    return "LetterMultiset(elements=$letterString)"
  }
}

private val alphaToPrime: BiMap<Char, Prime> = listOf(
  'e',
  't',
  'a',
  'o',
  'i',
  'n',
  's',
  'r',
  'h',
  'd',
  'l',
  'u',
  'c',
  'm',
  'f',
  'y',
  'w',
  'g',
  'p',
  'b',
  'v',
  'k',
  'x',
  'q',
  'j',
  'z',
).zip(Prime.first26()).toMap().toBiMap()
