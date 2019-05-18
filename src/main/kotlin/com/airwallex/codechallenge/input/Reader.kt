package com.airwallex.codechallenge.input

import com.airwallex.codechallenge.common.Marshalling
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import com.fasterxml.jackson.module.kotlin.readValue
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Stream

class Reader {

    private val mapper = Marshalling.mapper

    fun read(filename: String): Stream<CurrencyConversionRate> =
        read(Files.lines(Paths.get(filename)))

    fun read(lines: Stream<String>): Stream<CurrencyConversionRate> =
        lines.map {
            try {
                mapper.readValue<CurrencyConversionRate>(it)
            } catch (e: MissingKotlinParameterException) {
                CurrencyConversionRate.INVALID
            } catch (e: JsonParseException) {
                CurrencyConversionRate.INVALID
            } catch (e: InvalidDefinitionException) {
                CurrencyConversionRate.INVALID
            }
        }
}