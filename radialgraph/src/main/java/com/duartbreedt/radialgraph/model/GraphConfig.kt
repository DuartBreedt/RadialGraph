package com.duartbreedt.radialgraph.model

data class GraphConfig (
    val animationDirection: AnimationDirection,
    val labelsEnabled: Boolean,
    val strokeWidth: Float,
    // FIXME: Not working currently. Attend to this in https://github.com/DuartBreedt/RadialGraph/issues/6
    val capStyle: Cap
) {
    fun isClockwise(): Boolean = animationDirection == AnimationDirection.CLOCKWISE
    fun isCounterClockwise(): Boolean = animationDirection == AnimationDirection.COUNTERCLOCKWISE
}