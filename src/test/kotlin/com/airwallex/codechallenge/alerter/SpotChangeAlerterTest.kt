package com.airwallex.codechallenge.alerter

import com.airwallex.codechallenge.alerter.SpotAlerter.Companion.DOUBLE_ERROR
import com.airwallex.codechallenge.common.CurrencyPairs.EURUSD
import com.airwallex.codechallenge.common.CurrencyPairs.USDCAD
import com.airwallex.codechallenge.input.CurrencyConversionRate
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.offset
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SpotChangeAlerterTest {
    private lateinit var alerter: SpotChangeAlerter
    private val rate = CurrencyConversionRate.ofNow(EURUSD, 1.0)

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
        assertThat(alerter.process(rate, rate.next(), rate.next(rate = 1.11, offset = 2))).isNotEmpty
    }

    @Test
    fun `spot change of another pair not firing test`() {
        assertThat(alerter.process(rate, rate.next(), rate.next(rate = 1.11, offset = 2).with(USDCAD))).isEmpty()
    }

    @Test
    fun `out-of-scope spot changes not firing test`() {
        alerter = SpotChangeAlerter(averageDurationInSeconds = 2)

        assertThat(alerter.process(
            rate.next(offset = 0, rate = 1.10),
            rate.next(offset = 1, rate = 1.05), // avg: 1.10,  change: 0.045454545...
            rate.next(offset = 2, rate = 1.00), // avg: 1.075, change: 0.069767441860465
            rate.next(offset = 3, rate = 0.90)  // avg: 1.025, change: 0.1
        )).isNotEmpty

        assertThat(alerter.getStatusOf(EURUSD).average)
            .isCloseTo(0.95, offset(DOUBLE_ERROR))
    }
}