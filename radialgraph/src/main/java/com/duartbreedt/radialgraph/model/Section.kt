package com.duartbreedt.radialgraph.model

import androidx.annotation.ColorInt
import java.math.BigDecimal

class Section {
    val label: String?
    val displayMode: DisplayMode
    val value: BigDecimal
    @ColorInt
    val color: Int
    @ColorInt
    val gradientColors: List<Int>?
    lateinit var totalValue: BigDecimal
    lateinit var normalizedValue: BigDecimal
    lateinit var percent: BigDecimal

    constructor(value: BigDecimal, color: Int, gradientColors: List<Int>? = null) {
        this.label = null
        this.displayMode = DisplayMode.PERCENT
        this.value = value
        this.color = color
        this.gradientColors = gradientColors
    }

    constructor(label: String, value: BigDecimal, color: Int, gradientColors: List<Int>? = null) {
        this.label = label
        this.displayMode = DisplayMode.PERCENT
        this.value = value
        this.color = color
        this.gradientColors = gradientColors
    }

    constructor(displayMode: DisplayMode, value: BigDecimal, color: Int, gradientColors: List<Int>? = null) {
        this.label = null
        this.displayMode = displayMode
        this.value = value
        this.color = color
        this.gradientColors = gradientColors
    }

    enum class DisplayMode {
        PERCENT,
        VALUE
    }
}