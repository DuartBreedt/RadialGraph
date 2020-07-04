package com.duartbreedt.radialgraph.model

import com.duartbreedt.radialgraph.extensions.sumByBigDecimal
import java.math.BigDecimal
import java.math.RoundingMode

class GraphData(categories: List<GraphCategory>) {

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

