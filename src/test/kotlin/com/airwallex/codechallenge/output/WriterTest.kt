package com.airwallex.codechallenge.output

import com.airwallex.codechallenge.input.CurrencyConversionRate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant.ofEpochMilli

internal class WriterTest {
    private lateinit var writer: Writer

    @BeforeEach
    fun setup() {
        writer = Writer()
    }

    @Test
    fun `output format test`() {
        val rate = CurrencyConversionRate(ofEpochMilli(1554933784023), "CNYAUD", 0.39281)
        assertThat(writer.write(RateMoveAlert.from(rate)))
            .hasToString("{ \"timestamp\" : 1554933784.023, \"currencyPair\" : \"CNYAUD\", \"alert\" : \"spotChange\" }")
    }
}