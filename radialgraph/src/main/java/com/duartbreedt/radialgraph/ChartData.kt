package com.duartbreedt.radialgraph

import java.math.BigDecimal
import java.math.RoundingMode

class ChartData(
    categories: List<ChartCategory>
) {
    val categories: List<ChartCategory> = categories.map {
        it.apply {
            totalValue = calculateTotalValue(categories)
            normalizedValue = value.divide(totalValue, 10, RoundingMode.HALF_EVEN)
            percent = normalizedValue.multiply(BigDecimal("100")).setScale(1, RoundingMode.HALF_EVEN)
        }
    }

    val totalValue: BigDecimal = calculateTotalValue(categories)

    private fun calculateTotalValue(categories: List<ChartCategory>): BigDecimal =
        categories.sumByBigDecimal { it.value }
}

class ChartCategory {
    val label: String?
    val value: BigDecimal
    val color: Int
    lateinit var totalValue: BigDecimal
    lateinit var normalizedValue: BigDecimal
    lateinit var percent: BigDecimal

    constructor(
        label: String,
        value: BigDecimal,
        color: Int
    ) {
        this.label = label
        this.value = value
        this.color = color
    }

    constructor(
        value: BigDecimal,
        color: Int
    ) {
        this.label = null
        this.value = value
        this.color = color
    }

    fun calculateLabelPositionValue(portionStartPositionValue: BigDecimal): Float =
        portionStartPositionValue
            .minus(normalizedValue.divide(BigDecimal("2"), 2, RoundingMode.HALF_EVEN))
            .toFloat()
}