@file:OptIn(ExperimentalSerializationApi::class)

package com.omricat.countdown.preprocessor

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.map
import com.github.michaelbull.result.mapError
import com.github.michaelbull.result.mapResult
import com.github.michaelbull.result.onFailure
import com.omricat.countdown.model.LetterMultiset
import com.omricat.countdown.model.Word
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import kotlinx.serialization.protobuf.ProtoBuf
import kotlinx.serialization.serializer


internal typealias Dictionary = Map<LetterMultiset, List<Word>>

public object PreProcessor {

  @JvmStatic
  public fun main(args: Array<String>) {
    runPreprocessor().onFailure { System.err.println(it) }
  }

  /**
   * Runs the pre-processor on stdin, writing the processed output to stdout.
   * Expects the input to be UTF-8
   */
  public fun runPreprocessor(): Result<Unit, Error> {
    val stdin = System.`in`.bufferedReader()
    val letterMultisets =
      stdin.useLines { lines ->
        lines.asIterable().mapResult { line ->
          Word(line).map { word -> LetterMultiset.fromWord(word) to word }
        }.map { pairs ->
          pairs.groupBy(keySelector = { it.first },
                        valueTransform = { it.second }
          )
        }
      }
    return letterMultisets.mapError { Error.Wrapped(it) }
      .andThen { map ->
        System.out.writeBytes(map.encodeToByteArray())
        Ok(Unit)
      }
  }

  public sealed interface Error {
    @JvmInline
    public value class Wrapped(public val wrappedError: Any) :
      Error
  }

  internal val defaultProtoBuf by lazy { ProtoBuf }
}

internal fun Dictionary.encodeToByteArray(
  serializer: SerializationStrategy<Dictionary> = serializer(),
  protoBuf: ProtoBuf = PreProcessor.defaultProtoBuf
): ByteArray =
  protoBuf.encodeToByteArray(serializer, this)

