package com.omricat.countdown.model

import com.github.michaelbull.result.unwrap
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LetterMultisetTest {

  @Test
  fun `can check if multiset is subset of another`() {
    val set1 = LetterMultiset.fromWord("aabbc").unwrap()
    val set2 = LetterMultiset.fromWord("abc").unwrap()

    assertTrue(set2.isSubsetOf(set1))
  }

  @Test
  fun `anagrams are sub and supersets of each other`() {
    val set1 = LetterMultiset.fromWord("aabcc").unwrap()
    val set2 = LetterMultiset.fromWord("cabac").unwrap()

    assertTrue(set1.isSubsetOf(set2))
    assertTrue(set1.isSupersetOf(set2))
  }

  @Test
  fun `can distinguish when neither is subset of other`() {
    val set1 = LetterMultiset.fromWord("poker").unwrap()
    val set2 = LetterMultiset.fromWord("keeper").unwrap()

    assertFalse(set1.isSubsetOf(set2))
    assertFalse(set1.isSupersetOf(set2))
  }

  @Test
  fun `can compute all sub-multisets`() {
    val set = LetterMultiset.fromWord("aabc").unwrap()
    val subsets = set.subMultiSets()

    val expectedSubsets = listOf(
      "",
      "a", "b", "c",
      "aa", "ab", "aab", "ac", "aac",
      "bc",
      "abc", "aabc"
    ).map { LetterMultiset.fromWord(it).unwrap() }.toSet()

    assertEquals(expectedSubsets, subsets)
  }

}

