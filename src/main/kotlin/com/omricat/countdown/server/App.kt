package com.omricat.countdown.server

import com.omricat.countdown.dictionary.Dictionary
import org.http4k.contract.ContractRoute
import org.http4k.contract.contract
import org.http4k.contract.meta
import org.http4k.contract.security.NoSecurity
import org.http4k.contract.simple.SimpleJson
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.then
import org.http4k.filter.ServerFilters
import org.http4k.format.ConfigurableKotlinxSerialization
import org.http4k.format.KotlinxSerialization
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Http4kServer
import org.http4k.server.Jetty
import org.http4k.server.asServer

fun main() {
  val dictionary = Dictionary.loadFromResources()

  val port: Int = System.getenv("PORT")?.toInt() ?: 8080

  countdownServer(port, dictionary).start().block()
}

fun countdownServer(port: Int, dictionary: Dictionary): Http4kServer =
  ServerFilters.CatchLensFailure.then(
    routes("/" bind contract(dictionary))
  )
    .asServer(Jetty(port))

fun contract(
  dictionary: Dictionary,
  serialization: ConfigurableKotlinxSerialization = KotlinxSerialization
) = contract {
  renderer = SimpleJson(serialization)
  descriptionPath = "openapi.json"
  security = NoSecurity // Set for documentation purposes
  routes += listOf(checkWordRoute(dictionary), healthcheck())
}

fun healthcheck(): ContractRoute {
  val spec = "/healthcheck".meta {
    description = "responds with 200 if server is running"
  }.bindContract(Method.GET)
  return spec to { _: Request -> Response(Status.OK) }
}
