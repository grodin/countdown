package com.omricat.countdown.server

import com.omricat.countdown.dictionary.Dictionary
import org.http4k.client.OkHttp
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

internal abstract class EndToEndTest(internal val dictionary: Dictionary) {
  protected val client = OkHttp()
  private val server by lazy { countdownServer(8080, dictionary) }
  protected val baseUrl = "http://localhost:${server.port()}"

  @BeforeTest
  fun setup() {
    server.start()
  }

  @AfterTest
  fun teardown() {
    server.stop()
  }
}
