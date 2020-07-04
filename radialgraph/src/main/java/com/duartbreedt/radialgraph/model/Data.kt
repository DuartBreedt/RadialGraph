package com.duartbreedt.radialgraph.model

import com.duartbreedt.radialgraph.extensions.sumByBigDecimal
import java.math.BigDecimal
import java.math.RoundingMode

class Data(sections: List<Section>) {

    val sections: List<Section> = sections.map {
        it.apply {
            totalValue = calculateTotalValue(sections)
            normalizedValue = value.divide(totalValue, 10, RoundingMode.HALF_EVEN)
            percent = normalizedValue.multiply(BigDecimal("100")).setScale(1, RoundingMode.HALF_EVEN)
        }
    }

    private fun calculateTotalValue(sections: List<Section>): BigDecimal =
        sections.sumByBigDecimal { it.value }
}

