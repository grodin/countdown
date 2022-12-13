package com.omricat.countdown.model

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.unwrap
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class LetterMultisetTest {

  @Test
  fun `construction fails for too high scoring words`() {
    val result = LetterMultiset.fromWord("zxjzjxjzjjcjzjcjjvj")

    assertIs<Err<LetterMultiset.Error.ArithmeticError>>(result)
  }

  @Test
  fun `construction succeeds for longest word in dict`() {
    val result = LetterMultiset.fromWord("bulbophyllum")

    assertIs<Ok<*>>(result)
  }

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
  fun `invertible conversion to PrimePowerDecomposition`() {
    val set = LetterMultiset.fromWord("poker").unwrap()

    assertEquals(
      set,
      LetterMultiset.fromPrimePowerDecomposition(set.primePowerDecomposition)
        .unwrap()
    )
  }

  @Test
  fun `invertible conversion from PrimePowerDecomposition `() {
    val decomposition = PrimePowerDecomposition(
      mapOf(
        Prime(2) to 2,
        Prime(3) to 2,
        Prime(5) to 1,
      )
    )

    assertEquals(decomposition,
                 LetterMultiset.fromPrimePowerDecomposition(decomposition)
                   .unwrap().primePowerDecomposition
    )
  }

  @Test
  fun `LetterMultiset of empty string should have hash 1`() {
    assertEquals(1L, LetterMultiset.fromWord("").unwrap().hash)
  }

}

