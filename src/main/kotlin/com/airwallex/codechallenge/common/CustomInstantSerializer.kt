package com.airwallex.codechallenge.common

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.datatype.jsr310.DecimalUtils.toBigDecimal
import java.time.Instant

/**
 * With ObjectMapper.configure(WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, true), we can write Instant as timestamps.
 *
 * The problem of that approach is it will write all 9 decimals for nano seconds precision. Like '1554933784.023000000'
 * And this customized instant serialization is for rendering Instant with millisecond precision.
 *
 * Should you find a built-in solution to achieve the same goal, feel free to delete this class.
 *
 */
class CustomInstantSerializer : JsonSerializer<Instant>() {
    companion object {
        const val MILLISECOND_PRECISION: Int = 3
    }

    override fun serialize(value: Instant?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        if (value != null) {
            gen?.writeNumber(toBigDecimal(value.epochSecond, value.nano).setScale(MILLISECOND_PRECISION))
        }
    }
}