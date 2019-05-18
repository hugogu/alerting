package com.airwallex.codechallenge.alerter

import com.airwallex.codechallenge.input.CurrencyConversionRate
import com.airwallex.codechallenge.output.RateMoveAlert
import com.airwallex.codechallenge.output.RateMoveAlertType
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

/**
 * when the spot rate has been rising/falling for 15 minutes. This alert should be
 * throttled to only output once per minute and should report the length of time
 * of the rise/fall in seconds.
 */
class RaiseOrFallAlerter(
    /**
     * Represents the time threshold in seconds.
     */
    private val strikeThreshold: Int = 900,
    /**
     * Represents the alerting threshold in seconds. Ensures only one alert will be sent out during this time range.
     */
    private val alertThreshold: Int = 60
)
    : SpotAlerter<RaiseOrFallAlerter.RaisingAndFailingDetector> {

    init {
        assert(alertThreshold > 0)
        assert(strikeThreshold > 0)
    }

    override val currencyPairsStatus: ConcurrentMap<String, RaisingAndFailingDetector> = ConcurrentHashMap()

    override fun process(currencyRate: CurrencyConversionRate): RateMoveAlert? {
        val detector = getStatusOf(currencyRate.currencyPair) { RaisingAndFailingDetector() }

        return detector.checkAndAlert(currencyRate)
    }

    /**
     * Maintains raising and failing status for a currency pair.
     */
    inner class RaisingAndFailingDetector {
        internal var strike: Int = 0
        internal var lastRate: Double? = null

        internal fun checkAndAlert(currencyRate: CurrencyConversionRate): RateMoveAlert? {
            val currentRate = currencyRate.rate
            if (lastRate != null) {
                // The trend will be reserved when the rate stays unchanged.
                // TODO double check if this behaviour is expected.
                if (currentRate == lastRate) {
                    if (strike > 0) {
                        strike += 1
                    } else if (strike < 0) {
                        strike -= 1
                    }
                } else if (currentRate > lastRate!!) {
                    if (strike > 0) {
                        strike += 1
                    } else {
                        strike = 1
                    }
                } else if (currentRate < lastRate!!) {
                    if (strike < 0) {
                        strike -= 1
                    } else {
                        strike = -1
                    }
                }
            }

            lastRate = currentRate

            if (Math.abs(strike) >= strikeThreshold && strike % alertThreshold == 0) {
                val alertType = if (strike > 0) RateMoveAlertType.Raising else RateMoveAlertType.Failing
                return RateMoveAlert.from(currencyRate, alertType, Math.abs(strike))
            }

            return null
        }
    }
}