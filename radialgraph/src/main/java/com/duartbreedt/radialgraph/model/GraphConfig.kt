package com.duartbreedt.radialgraph.model

data class GraphConfig (
    val animationDirection: AnimationDirection,
    val labelsEnabled: Boolean
) {
    fun isClockwise(): Boolean = animationDirection == AnimationDirection.CLOCKWISE
    fun isCounterClockwise(): Boolean = animationDirection == AnimationDirection.COUNTERCLOCKWISE
}