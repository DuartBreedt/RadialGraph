package com.duartbreedt.radialgraph.model

import android.graphics.drawable.Drawable
import android.view.View.NO_ID
import androidx.annotation.ColorInt

internal data class GraphConfig(
    val animationDirection: AnimationDirection,
    val animationDuration: Long,
    var labelsEnabled: Boolean,
    @ColorInt val labelsColor: Int?,
    var strokeWidth: Float,
    val capStyle: Cap,
    @ColorInt var backgroundTrackColor: Int,
    var backgroundTrackDrawable: Drawable?,
    var graphNodeType: GraphNode,
    @ColorInt var graphNodeColor: Int,
    val graphNodeTextSize: Float,
    var graphNodeIcon: Drawable?,
    val gradientType: GradientType,
    val gradientFill: GradientFill
) {
    val isBackgroundTrackEnabled: Boolean
        get() = backgroundTrackColor != NO_ID || backgroundTrackDrawable != null

    fun isClockwise(): Boolean = animationDirection == AnimationDirection.CLOCKWISE
    fun isCounterClockwise(): Boolean = animationDirection == AnimationDirection.COUNTERCLOCKWISE

    fun isGradientEnabled(): Boolean = gradientType != GradientType.NONE
}