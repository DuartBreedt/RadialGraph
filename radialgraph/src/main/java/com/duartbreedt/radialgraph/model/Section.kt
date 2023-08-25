package com.duartbreedt.radialgraph.model

import androidx.annotation.ColorInt
import java.lang.IllegalArgumentException
import java.math.BigDecimal

class Section {
    val label: String?
    val displayMode: DisplayMode
    val value: BigDecimal

    @ColorInt
    val color: List<Int>
    lateinit var totalValue: BigDecimal
    lateinit var normalizedValue: BigDecimal
    lateinit var percent: BigDecimal

    companion object {
        private const val EMPTY_COLOR_EXCEPTION = "Color can not be empty!"
    }

    constructor(value: BigDecimal, vararg color: Int) {
        this.label = null
        this.displayMode = DisplayMode.PERCENT
        this.value = value
        this.color = if(color.isEmpty()) throw IllegalArgumentException(EMPTY_COLOR_EXCEPTION) else color.toList()
    }

    constructor(label: String, value: BigDecimal, vararg color: Int) {
        this.label = label
        this.displayMode = DisplayMode.PERCENT
        this.value = value
        this.color = if(color.isEmpty()) throw IllegalArgumentException(EMPTY_COLOR_EXCEPTION) else color.toList()
    }

    constructor(displayMode: DisplayMode, value: BigDecimal, vararg color: Int) {
        this.label = null
        this.displayMode = displayMode
        this.value = value
        this.color = if(color.isEmpty()) throw IllegalArgumentException(EMPTY_COLOR_EXCEPTION) else color.toList()
    }

    enum class DisplayMode {
        PERCENT,
        VALUE
    }
}