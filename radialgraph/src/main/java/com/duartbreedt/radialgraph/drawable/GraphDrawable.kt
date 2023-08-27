package com.duartbreedt.radialgraph.drawable

import android.graphics.*
import android.graphics.drawable.Drawable
import com.duartbreedt.radialgraph.model.AnimationDirection
import com.duartbreedt.radialgraph.model.GraphConfig
import com.duartbreedt.radialgraph.model.SectionState


internal abstract class GraphDrawable(
    open val graphConfig: GraphConfig,
    open val sectionStates: List<SectionState>
) : Drawable() {

    protected val startingRotation = -90f

    /**
     * Creates a paint object with a phase value to indicate the progress of the bar
     *
     * @param state The state for which we are creating a paint object
     */
    protected fun buildPhasedPathPaint(state: SectionState) {
        val phase: Float = state.length!! + state.currentProgress

        state.paint = Paint().apply {
            strokeWidth = graphConfig.strokeWidth
            pathEffect = DashPathEffect(floatArrayOf(state.length!!, state.length!!), phase)
            style = Paint.Style.STROKE
            flags = Paint.ANTI_ALIAS_FLAG
            strokeCap = graphConfig.capStyle.paintCapStyle
            color = state.color.first()
        }
    }

    /**
     * Updates the phase value
     *
     * @param state The state whose paint should be updated
     */
    protected fun updatePhasedPathPaint(state: SectionState) {
        val phase: Float = state.length!! + state.currentProgress
        state.paint?.let {
            it.pathEffect = DashPathEffect(floatArrayOf(state.length!!, state.length!!), phase)
        }
    }

    protected fun buildCircularPath(boundaries: RectF): Path {
        val path = Path()
        val sign = if (graphConfig.animationDirection == AnimationDirection.CLOCKWISE) -1 else 1
        path.addArc(boundaries, 0f, 360f * sign)

        // The starting position of the arc is undesirable, therefore set it explicitly
        rotatePath(path, startingRotation)
        return path
    }

    private fun rotatePath(path: Path, degrees: Float) {
        val matrix = Matrix()
        val bounds = RectF()
        path.computeBounds(bounds, true)
        matrix.postRotate(degrees, bounds.centerX(), bounds.centerY())
        path.transform(matrix)
    }

    protected fun calculateBoundaries() = RectF(
        bounds.left.toFloat() + (graphConfig.strokeWidth / 2f),
        bounds.top.toFloat() + (graphConfig.strokeWidth / 2f),
        bounds.right.toFloat() - (graphConfig.strokeWidth / 2f),
        bounds.bottom.toFloat() - (graphConfig.strokeWidth / 2f)
    )

    override fun getOpacity(): Int = PixelFormat.OPAQUE
}