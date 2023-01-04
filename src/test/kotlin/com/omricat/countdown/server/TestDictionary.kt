package com.omricat.countdown.server

import com.github.michaelbull.result.unwrap
import com.omricat.countdown.dictionary.Dictionary
import com.omricat.countdown.model.LetterMultiset
import com.omricat.countdown.model.Word

val hydrogen = Word("hydrogen").unwrap()
val TestDictionary = Dictionary(
  mapOf(
    LetterMultiset.fromWord(hydrogen) to listOf(hydrogen)
  )
)
