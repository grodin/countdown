package com.omricat.countdown.server

import org.http4k.core.Body
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.format.KotlinxSerialization
import org.http4k.format.KotlinxSerialization.auto
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.expect

internal class CheckProposedWordTest {

  private fun checkWordBaseRequest() = Request(GET, "/check-word")

  val client = contract(TestDictionary, KotlinxSerialization)

  @Test
  fun `calling check-word without letters or word gives bad request`() {
    expect(Status.BAD_REQUEST) {
      client(checkWordBaseRequest()).status
    }
  }

  @Test
  fun `calling check-word with empty letters gives bad request`() {
    expect(Status.BAD_REQUEST) {
      client(checkWordBaseRequest().query("letters", "")).status
    }
  }

  @Test
  fun `calling check-word with null letters gives bad request`() {
    expect(Status.BAD_REQUEST) {
      client(checkWordBaseRequest().query("letters", null)).status
    }
  }

  @Test
  fun `calling check-word with letters but no word gives bad request`() {
    expect(Status.BAD_REQUEST) {
      client(checkWordBaseRequest().query("letters", "abc")).status
    }
  }

  @Test
  fun `calling check-word gives correct positive answer`() {
    val response = client(
      checkWordBaseRequest()
        .query("letters", "hydrogenasdf")
        .query("word", "hydrogen")
    )
    assertEquals(Status.OK, response.status)

    val body = Body.auto<CheckWordResult>().toLens()(response)
    assertTrue(body.isValid)
  }

  @Test
  fun `check-word gives negative answer for non-dictionary word`() {
    val response = client(
      checkWordBaseRequest()
        .query("letters", "abc")
        .query("word", "a")
    )
    assertEquals(Status.OK, response.status)

    val body = Body.auto<CheckWordResult>().toLens()(response)
    assertFalse(body.isValid)
  }

  @Test
  fun `check-word gives negative answer for word not subset of letters`() {
    val response = client(
      checkWordBaseRequest()
        .query("letters", "abc")
        .query("word", "ajkl")
    )
    assertEquals(Status.OK, response.status)

    val body = Body.auto<CheckWordResult>().toLens()(response)
    assertFalse(body.isValid)
  }
}


