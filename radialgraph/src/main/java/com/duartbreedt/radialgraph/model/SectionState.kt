package com.duartbreedt.radialgraph.model

data class SectionState(
    val sweepSize: Float,
    val startPosition: Float,
    val color: Int,
    var currentProgress: Float = 0f
)