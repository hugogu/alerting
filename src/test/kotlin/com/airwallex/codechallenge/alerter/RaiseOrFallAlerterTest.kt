package com.airwallex.codechallenge.alerter

import com.airwallex.codechallenge.input.CurrencyConversionRate
import com.airwallex.codechallenge.output.RateMoveAlertType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RaiseOrFallAlerterTest {
    private lateinit var alerter: RaiseOrFallAlerter
    private val rate = CurrencyConversionRate.ofNow("EURUSD", 1.0)

    @BeforeEach
    fun setup() {
        alerter = RaiseOrFallAlerter(strikeThreshold = 3, alertThreshold = 1)
    }

    @Test
    fun `none raising nor failing test`() {
        (1L..10L)
            .map { rate.next(interval = it) }
            .forEach { alerter.process(it) }

        assertThat(alerter.strike).isEqualTo(0)
    }

    @Test
    fun `ever raising test`() {
        (0L..10L)
            .map { rate.next(interval = it, rate = it * 1.0) }
            .forEach { alerter.process(it) }

        assertThat(alerter.strike).isEqualTo(10)
    }

    @Test
    fun `flat in raising test`() {
        val alerts = hashMapOf(1L to 1.0, 2L to 2.0, 3L to 2.0, 4L to 3.0)
            .map { rate.next(interval = it.key, rate = it.value) }
            .let { alerter.process(it) }

        assertThat(alerter.strike).isEqualTo(3)
        assertThat(alerts).isNotEmpty
        assertThat(alerts).allMatch {
            it.alert == RateMoveAlertType.Raising && it.seconds == 3
        }
    }

    @Test
    fun `v-turn-like raising test`() {
        val alerts = hashMapOf(1L to 4.0, 2L to 2.0, 3L to 3.0, 4L to 4.0)
            .map { rate.next(interval = it.key, rate = it.value) }
            .let { alerter.process(it) }

        assertThat(alerter.strike).isEqualTo(2)
        assertThat(alerts).isEmpty()
    }
}