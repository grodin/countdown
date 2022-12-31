package com.omricat.countdown.model

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.map
import com.github.michaelbull.result.mapResult
import com.github.michaelbull.result.unwrap
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = Word.WordSerializer::class)
@JvmInline
public value class Word private constructor(public val value: List<Char>) {
  public companion object {
    public operator fun invoke(word: String): Result<Word, Error> =
      word.toList()
        .mapResult { c ->
          if (c in 'a'..'z') Ok(c) else Err(Error.NotEnglishLowercaseError(c))
        }.map { Word(it) }
  }

  public sealed interface Error {
    public data class NotEnglishLowercaseError(val char: Char) : Error
  }

  override fun toString(): String = value.joinToString("")

  internal object WordSerializer : KSerializer<Word> {
    override val descriptor: SerialDescriptor =
      PrimitiveSerialDescriptor("Word", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Word) {
      encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): Word =
      Word(decoder.decodeString()).unwrap()
  }
}
