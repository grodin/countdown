package com.omricat.countdown.dictionary

import com.omricat.countdown.model.LetterMultiset
import com.omricat.countdown.model.Word
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.protobuf.ProtoBuf
import kotlinx.serialization.serializer
import java.io.IOException

private const val DICTIONARY_FILE_NAME = "/dictionary_proto_encoded"

@JvmInline
value class Dictionary(private val dictionary: Map<LetterMultiset, List<Word>>) {
  operator fun get(letterMultiset: LetterMultiset): List<Word> =
    dictionary[letterMultiset] ?: emptyList()

  fun contains(word: Word): Boolean =
    dictionary[LetterMultiset.fromWord(word)]?.contains(word) ?: false

  @OptIn(ExperimentalSerializationApi::class)
  companion object {
    fun loadFromResources(): Dictionary =
      Dictionary(
        ProtoBuf.decodeFromByteArray(
          serializer(),
          Dictionary::class.java.getResource(DICTIONARY_FILE_NAME)
            ?.readBytes()
            ?: throw IOException("Unable to load dictionary from resources")
        )
      )
  }
}
