package com.duartbreedt.radialgraph.model

import androidx.annotation.ColorInt
import java.math.BigDecimal

class Section {
    val label: String?
    val displayMode: DisplayMode
    val value: BigDecimal

    @ColorInt
    val color: Int
    lateinit var totalValue: BigDecimal
    lateinit var normalizedValue: BigDecimal
    lateinit var percent: BigDecimal

    constructor(value: BigDecimal, color: Int) {
        this.label = null
        this.displayMode = DisplayMode.PERCENT
        this.value = value
        this.color = color
    }

    constructor(label: String, value: BigDecimal, color: Int) {
        this.label = label
        this.displayMode = DisplayMode.PERCENT
        this.value = value
        this.color = color
    }

    constructor(displayMode: DisplayMode, value: BigDecimal, color: Int) {
        this.label = null
        this.displayMode = displayMode
        this.value = value
        this.color = color
    }

    enum class DisplayMode {
        PERCENT,
        VALUE
    }
}