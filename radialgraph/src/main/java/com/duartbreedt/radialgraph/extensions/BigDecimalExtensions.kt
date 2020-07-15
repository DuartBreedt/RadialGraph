package com.duartbreedt.radialgraph.extensions

import java.math.BigDecimal
import java.text.DecimalFormat

const val percentageDecimalPattern: String = "##0.0"

/**
 * Adds the result together of an operation performed on each element in an iterable
 *
 * This function does not cater for null types
 *
 * @param selector The operation to be performed on each element in the iterable
 * @return The addition of all the results of performing an operation (selector) on every element in the iterable
 */
inline fun <T> Iterable<T>.sumByBigDecimal(selector: (T) -> BigDecimal): BigDecimal {
    var sum: BigDecimal = BigDecimal.ZERO

    for (element in this) {
        sum += selector(element)
    }

    return sum
}

fun BigDecimal.toFormattedDecimal(): String = createSpaceGroupingSeparator(this, percentageDecimalPattern)

private fun createSpaceGroupingSeparator(value: BigDecimal, pattern: String): String {
    val formatter = DecimalFormat(pattern)
    val formatSymbols = formatter.decimalFormatSymbols

    formatSymbols.groupingSeparator = ' '
    formatSymbols.decimalSeparator = '.'
    formatter.decimalFormatSymbols = formatSymbols

    return formatter.format(value)
}