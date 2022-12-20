package com.omricat.countdown.model

import com.github.michaelbull.result.unwrap
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LetterMultisetTest {

  @Test
  fun `can check if multiset is subset of another`() {
    val set1 = LetterMultiset.fromWord(Word("aabbc").unwrap())
    val set2 = LetterMultiset.fromWord(Word("abc").unwrap())

    assertTrue(set2.isSubsetOf(set1))
  }

  @Test
  fun `anagrams are sub and supersets of each other`() {
    val set1 = LetterMultiset.fromWord(Word("aabcc").unwrap())
    val set2 = LetterMultiset.fromWord(Word("cabac").unwrap())

    assertTrue(set1.isSubsetOf(set2))
    assertTrue(set1.isSupersetOf(set2))
  }

  @Test
  fun `can distinguish when neither is subset of other`() {
    val set1 = LetterMultiset.fromWord(Word("poker").unwrap())
    val set2 = LetterMultiset.fromWord(Word("keeper").unwrap())

    assertFalse(set1.isSubsetOf(set2))
    assertFalse(set1.isSupersetOf(set2))
  }

  @Test
  fun `can compute all sub-multisets`() {
    val set = LetterMultiset.fromWord(Word("aabc").unwrap())
    val subsets = set.subMultiSets()

    val expectedSubsets = listOf(
      "",
      "a", "b", "c",
      "aa", "ab", "aab", "ac", "aac",
      "bc",
      "abc", "aabc"
    ).map { LetterMultiset.fromWord(Word(it).unwrap()) }.toSet()

    assertEquals(expectedSubsets, subsets)
  }

}

