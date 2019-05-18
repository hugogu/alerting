package com.airwallex.codechallenge.alerter

import com.airwallex.codechallenge.input.CurrencyConversionRate
import com.airwallex.codechallenge.output.RateMoveAlertType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class RaiseOrFallAlerterTest {
    private val testPair = "EURUSD"
    private lateinit var alerter: RaiseOrFallAlerter
    private val rate = CurrencyConversionRate.ofNow(testPair, 1.0)

    @BeforeEach
    fun setup() {
        alerter = RaiseOrFallAlerter(strikeThreshold = 3, alertThreshold = 1)
    }

    @Test
    fun `none raising nor failing test`() {
        (1L..10L)
            .map { rate.next(interval = it) }
            .forEach { alerter.process(it) }

        assertThat(alerter.getStatusOf(testPair).strike).isEqualTo(0)
    }

    @Test
    fun `ever raising test`() {
        (1L..10L)
            .map { rate.next(interval = it, rate = it.toDouble()) }
            .forEach { alerter.process(it) }

        assertThat(alerter.getStatusOf(testPair).strike).isEqualTo(9)
    }

    @Test
    fun `flat in raising test`() {
        val alerts = hashMapOf(1L to 1.0, 2L to 2.0, 3L to 2.0, 4L to 3.0)
            .map { rate.next(interval = it.key, rate = it.value) }
            .let { alerter.process(it) }

        assertThat(alerter.getStatusOf(testPair).strike).isEqualTo(3)
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

        assertThat(alerter.getStatusOf(testPair).strike).isEqualTo(2)
        assertThat(alerts).isEmpty()
    }

    @ParameterizedTest
    @CsvSource(
        "1, 2, 3, 2, 1",
        "1, 2, 4, 3, 1",
        "1, 2, 5, 4, 2",
        "1, 2, 6, 5, 2",
        "1, 3, 4, 3, 1",
        "1, 3, 5, 4, 1",
        "1, 3, 6, 5, 1",
        "1, 3, 7, 6, 2",
        "3, 2, 3, 2, 0",
        "3, 2, 4, 3, 0",
        "3, 2, 5, 4, 1"
        )
    fun `alert threshold test`(strikeBar: Int, alertBar: Int, dataLength: Int, strikeExpected: Int, alertExpected: Int) {
        alerter = RaiseOrFallAlerter(strikeThreshold = strikeBar, alertThreshold = alertBar)

        val alerts = (1L..dataLength)
            .map { rate.next(interval = it, rate = it.toDouble()) }
            .let { alerter.process(it) }

        assertThat(alerter.getStatusOf(testPair).strike).isEqualTo(strikeExpected)
        assertThat(alerts.size).isEqualTo(alertExpected)
    }
}