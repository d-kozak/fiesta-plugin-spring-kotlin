package cz.esn.fiesta.fiestapluginspringkotlin.utils

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

private val mapper = jacksonObjectMapper()

internal inline fun <reified T> String.toDto() = mapper.readValue(this, T::class.java)

internal fun String.toJsonNode() = mapper.readTree(this)
