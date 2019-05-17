package com.airwallex.codechallenge.alerter

import com.airwallex.codechallenge.input.CurrencyConversionRate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SpotChangeAlerterTest {
    private lateinit var alerter: SpotChangeAlerter
    private val rate = CurrencyConversionRate.ofNow("EURUSD", 1.0)

    @BeforeEach
    fun setup() {
        alerter = SpotChangeAlerter(averageDurationInSeconds = 3)
    }

    @Test
    fun `first spot test`() {
        assertNull(alerter.process(rate))
    }

    @Test
    fun `spot change not firing test`() {
        assertThat(alerter.process(rate, rate.next(), rate.next(1.1, 2))).isEmpty()
    }

    @Test
    fun `spot change firing test`() {
        assertThat(alerter.process(rate, rate.next(), rate.next(rate = 1.11, interval = 2))).isNotEmpty
    }
}