package com.duartbreedt.radialgraph

import android.content.Context
import androidx.core.content.ContextCompat
import java.math.BigDecimal
import java.math.RoundingMode

class GraphData(
    categories: List<GraphCategory>
) {
    val categories: List<GraphCategory> = categories.map {
        it.apply {
            totalValue = calculateTotalValue(categories)
            normalizedValue = value.divide(totalValue, 10, RoundingMode.HALF_EVEN)
            percent = normalizedValue.multiply(BigDecimal("100")).setScale(1, RoundingMode.HALF_EVEN)
        }
    }

    private fun calculateTotalValue(categories: List<GraphCategory>): BigDecimal =
        categories.sumByBigDecimal { it.value }
}

class GraphCategory {
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

    fun toGraphValue(context: Context): GraphDrawable.GraphValue {
        val graphColor = ContextCompat.getColor(context, color)

        return if (value == BigDecimal.ZERO) GraphDrawable.GraphValue(0F, graphColor)
        else GraphDrawable.GraphValue(normalizedValue.toFloat(), graphColor)
    }
}