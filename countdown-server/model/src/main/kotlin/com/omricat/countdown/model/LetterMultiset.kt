package com.omricat.countdown.model

import com.github.michaelbull.result.unwrap
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * A class which represents the multiset of letters contained in an English word.
 */
@Serializable(with = LetterMultiset.LetterMultisetSerializer::class)
public class LetterMultiset
private constructor(private val letters: List<Pair<Char, UShort>> = emptyList()) {

  private val mapOfLetters: Map<Char, UShort> by lazy {
    letters.associate { it }
  }

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
  public fun isSupersetOf(other: LetterMultiset): Boolean =
    other.isSubsetOf(this)

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

  internal object LetterMultisetSerializer : KSerializer<LetterMultiset> {
    override val descriptor =
      PrimitiveSerialDescriptor("LetterMultiSet", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LetterMultiset) {
      val letterString =
        value.letters.joinToString(separator = "") { (c, count) ->
          "$c".repeat(count.toInt())
        }
      encoder.encodeString(letterString)
    }

    override fun deserialize(decoder: Decoder): LetterMultiset =
      fromWord(Word(decoder.decodeString()).unwrap())
  }
}
