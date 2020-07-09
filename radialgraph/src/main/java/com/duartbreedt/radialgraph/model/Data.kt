package com.duartbreedt.radialgraph.model

import android.content.Context
import androidx.core.content.ContextCompat
import com.duartbreedt.radialgraph.extensions.sumByBigDecimal
import java.math.BigDecimal
import java.math.RoundingMode

class Data(sections: List<Section>, total: BigDecimal? = null) {

    val total: BigDecimal = total ?: calculateTotalValue(sections)
    val sections: List<Section> = sections.map {

        val totalValue: BigDecimal = total ?: calculateTotalValue(sections)

        it.apply {
            this.totalValue = totalValue
            this.normalizedValue = value.divide(totalValue, 10, RoundingMode.HALF_EVEN)
            this.percent = normalizedValue.multiply(BigDecimal("100")).setScale(1, RoundingMode.HALF_EVEN)
        }
    }

    private fun calculateTotalValue(sections: List<Section>): BigDecimal =
        sections.sumByBigDecimal { it.value }

    fun toSectionStates(context: Context, graphConfig: GraphConfig): List<SectionState> {
        var previousSectionEndPosition = 0f

        val sectionStates = mutableListOf<SectionState>()

        sections.forEachIndexed { index: Int, section: Section ->
            val graphColor = ContextCompat.getColor(context, section.color)

            val sectionState: SectionState =
                if (section.value == BigDecimal.ZERO) SectionState(0f, 0f, graphColor, index == sections.size - 1)
                else SectionState(section.normalizedValue.toFloat(), previousSectionEndPosition, graphColor, index == sections.size - 1)

            previousSectionEndPosition += section.normalizedValue.toFloat()

            sectionStates.add(sectionState)
        }

        return sectionStates
    }
}

