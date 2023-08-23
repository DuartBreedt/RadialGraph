package com.duartbreedt.radialgraph.model

import com.duartbreedt.radialgraph.exceptions.GraphConfigException
import com.duartbreedt.radialgraph.extensions.sumByBigDecimal
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * @param total If not specified it is assumed that the sum of the sections comprise 100% of the graph
 */
class Data(sections: List<Section>, total: BigDecimal? = null) {

    val sections: List<Section> = sections.map {

        val calculatedTotal: BigDecimal = calculateTotalValue(sections)

        if (total != null && total < calculatedTotal) {
            throw GraphConfigException("Specified total exceeds the sum of the section values!")
        }

        val totalValue: BigDecimal = total ?: calculatedTotal

        it.apply {
            this.totalValue = totalValue
            this.normalizedValue = value.divide(totalValue, 10, RoundingMode.HALF_EVEN)
            this.percent =
                normalizedValue.multiply(BigDecimal("100")).setScale(1, RoundingMode.HALF_EVEN)
        }
    }

    private fun calculateTotalValue(sections: List<Section>): BigDecimal =
        sections.sumByBigDecimal { it.value }

    fun toSectionStates(): List<SectionState> {
        var previousSectionEndPosition = 0f

        val sectionStates = mutableListOf<SectionState>()

        sections.forEachIndexed { index: Int, section: Section ->
            val sectionState: SectionState =
                if (section.value == BigDecimal.ZERO) SectionState(
                    0f,
                    0f,
                    section.color,
                    index == sections.size - 1
                )
                else SectionState(
                    section.normalizedValue.toFloat(),
                    previousSectionEndPosition,
                    section.color,
                    index == sections.size - 1,
                    section.gradientColors
                )

            previousSectionEndPosition += section.normalizedValue.toFloat()

            sectionStates.add(sectionState)
        }

        return sectionStates
    }
}

