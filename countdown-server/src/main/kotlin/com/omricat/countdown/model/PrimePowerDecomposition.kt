package com.omricat.countdown.model

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapError
import com.github.michaelbull.result.runCatching
import kotlin.collections.fold
import kotlin.collections.map
import kotlin.streams.toList
import com.github.michaelbull.result.fold as foldResult

private typealias Decomposition = List<Pair<Prime, Int>>

/**
 * Immutable class representing a decomposition of a natural number into its
 * product of powers of primes.
 */
class PrimePowerDecomposition private constructor(internal val powers: Decomposition) {

  internal constructor(powers: Map<Prime, Int>) : this(
    powers.toList().sortedBy { it.first })

  fun computeValue(): Result<Long, ArithmeticError> =
    powers.map { (prime, power) -> prime.power(power) }.product()

  fun allDivisors(): Set<PrimePowerDecomposition> =
    (0 until 1.shl(powers.size)).map {
      BitSet.from(it)
    }.flatMap {
      divisorsByBitset(it)
    }.toSet()

  object ArithmeticError

  private fun Prime.power(power: Int): Long {
    var acc = 1L
    repeat(power) {
      acc *= value
    }
    return acc
  }

  private fun Iterable<Long>.product(): Result<Long, ArithmeticError> =
    foldResult(1L) { acc, t ->
      runCatching {
        Math.multiplyExact(acc, t)
      }
    }.mapError { ArithmeticError }

  private fun divisorsByBitset(
    bitSet: BitSet
  ): List<PrimePowerDecomposition> {
    val selectedPrimes =
      bitSet.indicesOfSetBits()
        .map { idx -> powers[idx].first to powers[idx].second }
    return selectedPrimes.fold(
      listOf<Decomposition>(listOf())
    ) { acc, (prime, power) ->
      acc.flatMap { decomp ->
        (1..power).map { decomp + (prime to it) }
      }
    }.map { PrimePowerDecomposition(it) }
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as PrimePowerDecomposition

    if (powers != other.powers) return false

    return true
  }

  override fun hashCode(): Int {
    return powers.hashCode()
  }

  override fun toString(): String {
    return "PrimePowerDecomposition(powers=$powers)"
  }

  @JvmInline
  private value class BitSet private constructor(private val bits: java.util.BitSet) {
    constructor(numBits: Int) : this(
      java.util.BitSet().apply { set(0, numBits) }
    )

    fun indicesOfSetBits(): List<Int> = bits.stream().toList()

    companion object {
      fun from(value: Int): BitSet =
        BitSet(java.util.BitSet.valueOf(longArrayOf(value.toLong())))
    }
  }
}
