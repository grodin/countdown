package com.omricat.countdown.dictionary

import com.omricat.countdown.model.LetterMultiset
import com.omricat.countdown.model.Word
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.serializer
import java.io.IOException

private const val DICTIONARY_FILE_NAME = "/dictionary.json"

@JvmInline
value class Dictionary(private val dictionary: Map<LetterMultiset, List<Word>>) {
  operator fun get(letterMultiset: LetterMultiset): List<Word> =
    dictionary[letterMultiset] ?: emptyList()

  fun contains(word: Word): Boolean =
    get(LetterMultiset.fromWord(word)).contains(word)

  @OptIn(ExperimentalSerializationApi::class)
  companion object {
    fun loadFromResources(): Dictionary =
      Dictionary(
        Json.decodeFromStream(
          serializer(),
          Dictionary::class.java.getResourceAsStream(DICTIONARY_FILE_NAME)

            ?: throw IOException("Unable to load dictionary from resources")
        )
      )
  }
}
