package com.airwallex.codechallenge.alerter

import com.airwallex.codechallenge.input.CurrencyConversionRate
import com.airwallex.codechallenge.output.RateMoveAlert

class RaiseOrFallAlerter : SpotAlerter {
    override fun process(currencyRate: CurrencyConversionRate): RateMoveAlert? {
        return null
    }
}