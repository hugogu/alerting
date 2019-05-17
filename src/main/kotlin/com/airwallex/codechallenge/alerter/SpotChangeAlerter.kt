package com.airwallex.codechallenge.alerter

import com.airwallex.codechallenge.input.CurrencyConversionRate
import com.airwallex.codechallenge.output.RateMoveAlert
import org.apache.commons.collections4.queue.CircularFifoQueue
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

/**
 * When the spot rate for a currency pair changes by more than 10% from the 5 minute average for that currency pair
 */
class SpotChangeAlerter(private val averageDurationInSeconds: Int = 300, private val moveTolerance: Double = 0.1) :
    SpotAlerter {
    private var currencyPairAverages: ConcurrentMap<String, CurrencyPairHistoryRates> = ConcurrentHashMap()

    override fun process(currencyRate: CurrencyConversionRate): RateMoveAlert? {
        val rateHistory = currencyPairAverages.computeIfAbsent(currencyRate.currencyPair) { CurrencyPairHistoryRates() }
        val averageRate = rateHistory.average
        rateHistory.addRate(currencyRate.rate)
        val changeRate = Math.abs(averageRate - currencyRate.rate) / averageRate
        return if (averageRate > 0 && changeRate - moveTolerance > doubleErrorTolerance) {
            RateMoveAlert.from(currencyRate)
        } else {
            null
        }
    }

    /**
     * Maintains history rates and calculates average for a currency pair.
     */
    private inner class CurrencyPairHistoryRates {
        private val historyRates: CircularFifoQueue<Double> = CircularFifoQueue(averageDurationInSeconds)
        var average: Double = 0.0

        internal fun addRate(rate: Double) {
            average = when (historyRates.isAtFullCapacity) {
                true -> {
                    (average * historyRates.size - historyRates.peek() + rate) / historyRates.size
                }
                false -> {
                    (average * historyRates.size + rate) / (historyRates.size + 1)
                }
            }
            historyRates.add(rate)
        }
    }
}