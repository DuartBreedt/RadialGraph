package com.duartbreedt.radialgraph.model

import android.graphics.drawable.Drawable
import android.view.View.NO_ID
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes

data class GraphConfig(
    val animationDirection: AnimationDirection,
    val animationDuration: Long,
    val labelsEnabled: Boolean,
    @ColorInt val labelsColor: Int?,
    val strokeWidth: Float,
    val capStyle: Cap,
    @ColorInt val backgroundTrackColor: Int,
    val backgroundTrackDrawable: Drawable?,
    val graphNodeType: GraphNode,
    @ColorInt val graphNodeColor: Int,
    val graphNodeTextSize: Float
) {
    val isBackgroundTrackEnabled = backgroundTrackColor != NO_ID || backgroundTrackDrawable != null

    fun isClockwise(): Boolean = animationDirection == AnimationDirection.CLOCKWISE
    fun isCounterClockwise(): Boolean = animationDirection == AnimationDirection.COUNTERCLOCKWISE
}