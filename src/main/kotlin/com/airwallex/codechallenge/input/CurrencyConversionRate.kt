package com.airwallex.codechallenge.input

import java.time.Instant

data class CurrencyConversionRate(
    val timestamp: Instant,
    val currencyPair: String,
    val rate: Double
) {
    companion object Factory {
        fun ofNow(currencyPair: String, rate: Double) =
            CurrencyConversionRate(Instant.now(), currencyPair, rate)
    }

    fun next(rate: Double = this.rate, interval: Long = 1L) =
        CurrencyConversionRate(timestamp.plusSeconds(interval), currencyPair, rate)
}
