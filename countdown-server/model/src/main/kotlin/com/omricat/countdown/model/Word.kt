package com.omricat.countdown.model

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.map
import com.github.michaelbull.result.mapResult

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
}
