package com.omricat.countdown.model

/**
 * Represents a prime number. Construction is not checked, so could hold anything
 */
@JvmInline
public value class Prime internal constructor(public val value: Int) : Comparable<Prime> {
  public companion object {

    public fun first26(): List<Prime> = listOf(
      2,
      3,
      5,
      7,
      11,
      13,
      17,
      19,
      23,
      29,
      31,
      37,
      41,
      43,
      47,
      53,
      59,
      61,
      67,
      71,
      73,
      79,
      83,
      89,
      97,
      101,
    ).map { Prime(it) }
  }

  override fun compareTo(other: Prime): Int =
    this.value.compareTo(other.value)
}
