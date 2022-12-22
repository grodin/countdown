package com.omricat.countdown.server

import com.omricat.countdown.dictionary.Dictionary
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.server.Http4kServer
import org.http4k.server.Jetty
import org.http4k.server.asServer

fun main() {
  val dictionary = Dictionary.loadFromResources()

  val port: Int = System.getenv("PORT")?.toInt() ?: 8080

  countdownServer(port, dictionary).start().block()
}

fun countdownServer(port: Int, dictionary: Dictionary): Http4kServer =
  { _: Request -> Response(Status.OK) }.asServer(Jetty(port))

