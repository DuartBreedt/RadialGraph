package com.duartbreedt.radialgraph.model

import android.content.Context
import androidx.core.content.ContextCompat
import java.math.BigDecimal
import java.math.RoundingMode

class Section {
    val label: String?
    val value: BigDecimal
    val color: Int
    lateinit var totalValue: BigDecimal
    lateinit var normalizedValue: BigDecimal
    lateinit var percent: BigDecimal

    constructor(label: String, value: BigDecimal, color: Int) {
        this.label = label
        this.value = value
        this.color = color
    }

    constructor(value: BigDecimal, color: Int) {
        this.label = null
        this.value = value
        this.color = color
    }

    fun calculateLabelPositionValue(portionStartPositionValue: BigDecimal): Float =
        portionStartPositionValue
            // CCW
            // .minus(normalizedValue.divide(BigDecimal("2"), 2, RoundingMode.HALF_EVEN))

            // CW
            .plus(normalizedValue.divide(BigDecimal("2"), 2, RoundingMode.HALF_EVEN))
            .toFloat()

    fun toGraphValue(context: Context): SectionState {
        val graphColor = ContextCompat.getColor(context, color)

        return if (value == BigDecimal.ZERO) SectionState(0F, graphColor)
        else SectionState(normalizedValue.toFloat(), graphColor)
    }
}