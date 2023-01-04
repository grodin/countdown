package com.omricat.countdown.server

import com.omricat.countdown.dictionary.Dictionary
import com.omricat.countdown.model.LetterMultiset
import kotlinx.serialization.Serializable
import org.http4k.contract.ContractRoute
import org.http4k.contract.meta
import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.format.KotlinxSerialization.auto
import org.http4k.lens.Query

fun checkWordRoute(dictionary: Dictionary): ContractRoute {
  val wordQueryLens = Query.word().required("word")
  val lettersQueryLens = Query.nonEmptyWord().required("letters")

  val spec = "/check-word" meta {
    description =
      "Checks whether word given in query can be formed with the given letters"
    queries += listOf(wordQueryLens, lettersQueryLens)
  } bindContract Method.GET

  val checkWordResultBody = Body.auto<CheckWordResult>().toLens()

  fun checkWord(): HttpHandler = { request: Request ->
    val letters = LetterMultiset.fromWord((lettersQueryLens(request)))
    val word = wordQueryLens(request)
    val wordLetterMultiset = LetterMultiset.fromWord(word)

    if (wordLetterMultiset.isSubsetOf(letters) && dictionary[wordLetterMultiset].contains(word))
      Response(Status.OK).with(checkWordResultBody of CheckWordResult(isValid = true))
    else
      Response(Status.OK).with(checkWordResultBody of CheckWordResult(isValid = false))
  }

  return spec to ::checkWord
}

@Serializable
data class CheckWordResult(val isValid: Boolean)
