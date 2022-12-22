package com.omricat.countdown.server

import com.github.michaelbull.result.getOrThrow
import com.github.michaelbull.result.mapError
import com.omricat.countdown.model.Word
import org.http4k.lens.BiDiLensSpec
import org.http4k.lens.nonEmptyString

fun <IN : Any> BiDiLensSpec<IN, String>.nonEmptyWord() = nonEmptyString().word()
fun <IN : Any> BiDiLensSpec<IN, String>.word() = map(String::toWord)
private fun String.toWord(): Word =
  Word(this)
    .mapError { IllegalArgumentException(it.toString()) }
    .getOrThrow()
