package com.airwallex.codechallenge.input

import org.apache.commons.lang3.StringUtils.EMPTY
import org.apache.commons.lang3.StringUtils.isEmpty
import java.lang.Double.isNaN
import java.time.Instant

data class CurrencyConversionRate(
    val timestamp: Instant,
    val currencyPair: String,
    val rate: Double
) {
    companion object Factory {
        fun ofNow(currencyPair: String, rate: Double) =
            CurrencyConversionRate(Instant.now(), currencyPair, rate)

        /**
         * INVALID object represents INVALID input.
         * TODO(Find a better choice)
         * TODO(Alert when invalid input detected.)
         */
        val INVALID = CurrencyConversionRate(Instant.MIN, EMPTY, Double.NaN)
    }

    init {
        assert(rate > 0.0 || isNaN(rate))
        assert(currencyPair.length == 6 || isEmpty(currencyPair))
    }

    fun next(rate: Double = this.rate, offset: Long = 1L) =
        CurrencyConversionRate(timestamp.plusSeconds(offset), currencyPair, rate)

    fun with(newPair: String) =
        CurrencyConversionRate(timestamp, newPair, rate)
}
