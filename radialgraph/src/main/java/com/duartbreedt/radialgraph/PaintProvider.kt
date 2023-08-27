package com.duartbreedt.radialgraph

import android.graphics.Paint
import androidx.annotation.ColorInt

internal class PaintProvider private constructor() {

    companion object {

        private var instances: MutableList<Paint> = mutableListOf()

        fun getPaint(@ColorInt colorInt: Int): Paint {
            return instances.firstOrNull { it.color == colorInt } ?: run {
                Paint().apply {
                    color = colorInt
                    style = Paint.Style.FILL
                    flags = Paint.ANTI_ALIAS_FLAG
                }.also { instances.add(it) }
            }
        }
    }
}