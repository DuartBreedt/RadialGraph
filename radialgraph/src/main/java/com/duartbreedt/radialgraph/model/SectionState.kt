package com.duartbreedt.radialgraph.model

import android.graphics.Paint
import android.graphics.Path

data class SectionState(
    val sweepSize: Float,
    val startPosition: Float,
    val color: Int,
    val isLastSection: Boolean,
    var currentProgress: Float = 0f,
    var path: Path? = null,
    var paint: Paint? = null,
    var length: Float? = null
)

enum class Cap(val paintCapStyle: Paint.Cap) {
    BUTT(Paint.Cap.BUTT),
    ROUND(Paint.Cap.ROUND),
    SQUARE(Paint.Cap.SQUARE)
}