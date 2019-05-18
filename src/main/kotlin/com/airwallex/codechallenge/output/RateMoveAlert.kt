package com.airwallex.codechallenge.output

import com.airwallex.codechallenge.common.CustomInstantSerializer
import com.airwallex.codechallenge.input.CurrencyConversionRate
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.time.Instant

data class RateMoveAlert(
    @JsonSerialize(using = CustomInstantSerializer::class)
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

    init {
        assert(seconds == null || seconds > 0)
        assert(currencyPair.length == 6)
    }
}