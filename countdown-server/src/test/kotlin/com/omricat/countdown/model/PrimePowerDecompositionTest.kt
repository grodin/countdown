package com.omricat.countdown.model

import com.github.michaelbull.result.unwrap
import kotlin.test.Test
import kotlin.test.assertEquals

class PrimePowerDecompositionTest {

  @Test
  fun `allDivisorsWorks`() {
    val decomposition = PrimePowerDecomposition(
      mapOf(
        Prime(2) to 2,
        Prime(3) to 2,
        Prime(5) to 1,
      )
    )


    val divisors = decomposition.allDivisors()
      .map { it.computeValue().unwrap() }.toSet()

    val expected = listOf(
      1, 2, 4, 3, 9, 6, 18, 12, 36, 5, 10, 20, 15, 45, 30, 90, 60, 180
    ).map { it.toLong() }.toSet()

    assertEquals(
      expected,
      divisors
    )

  }

  @Test
  fun `PrimePowerDecomposition from empty map should have value 1`() {
    assertEquals(
      1L,
      PrimePowerDecomposition(emptyMap()).computeValue().unwrap()
    )
  }

}
