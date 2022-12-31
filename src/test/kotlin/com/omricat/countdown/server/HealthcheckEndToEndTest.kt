package com.omricat.countdown.server

import com.omricat.countdown.dictionary.Dictionary
import org.http4k.client.OkHttp
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.metrics.MetricsDefaults.Companion.server
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.expect


internal class HealthcheckEndToEndTest : EndToEndTest(Dictionary(emptyMap())) {

  @Test
  fun `responds to healthcheck`() {
    expect(Status.OK) {
      client(
        Request(
          Method.GET,
          "$baseUrl/healthcheck"
        )
      ).status
    }
  }
}
