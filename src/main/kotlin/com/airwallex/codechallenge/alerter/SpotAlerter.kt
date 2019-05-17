package com.airwallex.codechallenge.alerter

import com.airwallex.codechallenge.input.CurrencyConversionRate
import com.airwallex.codechallenge.output.RateMoveAlert

/**
 * An abstraction of all alert rules that deals with spot update.
 */
interface SpotAlerter {
    /**
     * Process a rate update and tell if there should an alert thrown out.
     *
     * The implementation may assume that for each currency pair,
     * currency conversion rates are streamed at a constant rate of one per second
     */
    fun process(currencyRate: CurrencyConversionRate) : RateMoveAlert?
}