package com.duartbreedt.radialgraph.model

import androidx.annotation.ColorInt
import java.math.BigDecimal

class Section {
    val label: String?
    val value: BigDecimal
    @ColorInt
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
}