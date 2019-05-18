package com.airwallex.codechallenge.alerter

import com.airwallex.codechallenge.input.CurrencyConversionRate
import com.airwallex.codechallenge.output.RateMoveAlert
import com.airwallex.codechallenge.output.RateMoveAlertType
import java.util.*

/**
 * when the spot rate has been rising/falling for 15 minutes. This alert should be
 * throttled to only output once per minute and should report the length of time
 * of the rise/fall in seconds.
 */
class RaiseOrFallAlerter(private val strikeThreshold: Int = 900, private val alertThreshold: Int = 60) : SpotAlerter {
    internal var strike : Int = 0
    internal var lastRate: Optional<Double> = Optional.empty()

    override fun process(currencyRate: CurrencyConversionRate): RateMoveAlert? {
        val currentRate = currencyRate.rate
        if (lastRate.isPresent) {
            if (currentRate == lastRate.get()) {
                if (strike > 0) {
                    strike += 1
                } else if (strike < 0) {
                    strike -= 1
                }
            } else if (currentRate > lastRate.get()) {
                if (strike > 0) {
                    strike += 1
                } else {
                    strike = 1
                }
            } else if (currentRate < lastRate.get()) {
                if (strike < 0) {
                    strike -= 1
                } else {
                    strike = -1
                }
            }
        }

        lastRate = Optional.of(currentRate)

        if (Math.abs(strike) >= strikeThreshold && strike % alertThreshold == 0) {
            val alertType = if (strike > 0) RateMoveAlertType.Raising else RateMoveAlertType.Failing
            return RateMoveAlert.from(currencyRate, alertType, Math.abs(strike))
        }

        return null
    }
}