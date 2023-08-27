package com.duartbreedt.radialgraph

import android.graphics.Paint
import androidx.annotation.ColorInt

internal class TextPaintProvider private constructor() {

    companion object {

        private var instances: MutableList<Paint> = mutableListOf()

        fun getPaint(@ColorInt colorInt: Int, textSize: Float): Paint {
            return instances.firstOrNull { it.color == colorInt && it.textSize == textSize } ?: run {
                Paint().apply {
                    this.textSize = textSize
                    color = colorInt
                    flags = Paint.ANTI_ALIAS_FLAG
                }.also { instances.add(it) }
            }
        }
    }
}