package com.omricat.countdown.model

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.map
import com.github.michaelbull.result.mapError
import com.github.michaelbull.result.mapResult
import com.github.michaelbull.result.unwrap
import com.omricat.countdown.model.LetterMultiset.Error.ArithmeticError
import com.omricat.countdown.model.LetterMultiset.Error.NotEnglishLowercaseError

/**
 * A class which represents the multiset of letters contained in an English word.
 */
class LetterMultiset
private constructor(private val elements: List<Char>) :
  Collection<Char> by elements {

  private val hashResult: Result<Long, ArithmeticError> by lazy { computePrimePowerHash() }

  private fun computePrimePowerHash(): Result<Long, ArithmeticError> =
    primePowerDecomposition.computeValue().mapError { ArithmeticError }


  val primePowerDecomposition by lazy {
    computePrimePowers()
  }

  private fun computePrimePowers(): PrimePowerDecomposition =
    elements.groupBy { c: Char ->
      alphaToPrime.getValue(c)
    }.mapValues { (_, list) -> list.size }
      .let { PrimePowerDecomposition(it) }

  val hash get() = hashResult.unwrap()

  companion object {
    fun fromWord(word: String): Result<LetterMultiset, Error> =
      word.toList()
        .sorted()
        .mapResult { c ->
          if (c in 'a'..'z') Ok(c) else Err(
            NotEnglishLowercaseError(c)
          )
        }
        .map { LetterMultiset(it) }

        // Compute the hash in advance so that construction fails fast if
        // the hash is too big to fit in a long
        .andThen { multiset -> multiset.hashResult.map { multiset } }

    fun fromPrimePowerDecomposition(decomposition: PrimePowerDecomposition):
        Result<LetterMultiset, Error> =
      LetterMultiset.fromWord(
        decomposition.powers.flatMap { (prime, power) ->
          List(power) { alphaToPrime.inverseBiMap().getValue(prime) }
        }
          .joinToString(separator = "")
      )

  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as LetterMultiset

    if (elements!= other.elements) return false

    return true
  }

  override fun hashCode(): Int {
    return elements.hashCode()
  }

  override fun toString(): String {
    return "LetterMultiset(elements=$elements)"
  }

  sealed interface Error {

    data class NotEnglishLowercaseError(val char: Char) : Error
    object ArithmeticError : Error
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
