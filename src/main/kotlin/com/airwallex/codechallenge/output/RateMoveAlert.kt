package com.airwallex.codechallenge.output

import com.airwallex.codechallenge.input.CurrencyConversionRate
import java.time.Instant

data class RateMoveAlert(
    val timestamp: Instant,
    val currencyPair: String,
    val alert: RateMoveAlertType,
    val seconds: Int?
) {
    companion object Factory {
        fun from(
            rate: CurrencyConversionRate,
            alert: RateMoveAlertType = RateMoveAlertType.SpotChange,
            seconds: Int? = null
        ) =
            RateMoveAlert(rate.timestamp, rate.currencyPair, alert, seconds)
    }
}