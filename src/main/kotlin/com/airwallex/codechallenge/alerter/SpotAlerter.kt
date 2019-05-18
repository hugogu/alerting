package com.airwallex.codechallenge.alerter

import com.airwallex.codechallenge.input.CurrencyConversionRate
import com.airwallex.codechallenge.output.RateMoveAlert

/**
 * An abstraction of all alert rules that deals with spot update.
 */
interface SpotAlerter {
    val doubleErrorTolerance: Double
        get() = 0.0000001

    /**
     * Process a rate update and tell if there should an alert thrown out.
     *
     * The implementation may assume that for each currency pair,
     * currency conversion rates are streamed at a constant rate of one per second
     */
    fun process(currencyRate: CurrencyConversionRate) : RateMoveAlert?

    fun process(rates: Iterable<CurrencyConversionRate>) : Collection<RateMoveAlert> {
        val result = ArrayList<RateMoveAlert>()
        for (rate in rates) {
            process(rate)?.let { result.add(it) }
        }

        return result
    }

    fun process(vararg rates: CurrencyConversionRate) : Collection<RateMoveAlert> {
        return process(rates.toList())
    }
}