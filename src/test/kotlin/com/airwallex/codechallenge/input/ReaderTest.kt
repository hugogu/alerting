package com.airwallex.codechallenge.input

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.time.Instant
import java.util.stream.Stream
import kotlin.streams.toList

internal class ReaderTest {

    companion object {
        private const val VALID_JSON = "{ \"timestamp\": 1554933784.023, \"currencyPair\": \"CNYAUD\", \"rate\": 0.39281 }"
        private const val INVALID_JSON_MISSPAIR = "{ \"timestamp\": 1554933784.023, \"rate\": 0.39281 }"
        private const val INVALID_JSON_SHORTPAIR = "{ \"timestamp\": 1554933784.023, \"currencyPair\": \"CNY\", \"rate\": 0.39281 }"
        private const val INVALID_JSON_MALFORMED = "{"
    }

    @Nested
    inner class ReadStreamOfString {

        private lateinit var reader: Reader

        @BeforeEach
        fun setup() {
            reader = Reader()
        }

        @Test
        fun `when stream is valid`() {
            val result = reader.read(Stream.of(VALID_JSON)).toList()
            assertThat(result).isNotEmpty
            assertThat(result).first().isEqualTo(CurrencyConversionRate(
                timestamp = Instant.ofEpochSecond(1554933784, 23_000_000),
                currencyPair = "CNYAUD",
                rate = 0.39281
            ))
        }

        @ParameterizedTest
        @CsvSource(INVALID_JSON_MISSPAIR, INVALID_JSON_SHORTPAIR, INVALID_JSON_MALFORMED)
        fun `when stream is invalid`(json: String) {
            val result = reader.read(Stream.of(json)).toList()
            assertThat(result).isNotEmpty
            assertThat(result).first().isEqualTo(CurrencyConversionRate.INVALID)
        }
    }

}