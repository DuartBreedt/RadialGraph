package com.duartbreedt.radialgraph.model

import android.graphics.Paint
import android.graphics.Path

data class SectionState(

    // The size of the section as a percentage (0.0 - 1.0) of the entire graph
    val sweepSize: Float,

    // The start position of this section's sweep as a value between 0.0 and 1.0
    val startPosition: Float,

    val color: Int,
    val isLastSection: Boolean,

    // Measured section progress length
    var currentProgress: Float = 0f,

    var path: Path? = null,
    var paint: Paint? = null,

    // Measured path length of the entire graph
    var length: Float? = null
)

enum class Cap(val paintCapStyle: Paint.Cap) {
    BUTT(Paint.Cap.BUTT),
    ROUND(Paint.Cap.ROUND),
    SQUARE(Paint.Cap.SQUARE)
}