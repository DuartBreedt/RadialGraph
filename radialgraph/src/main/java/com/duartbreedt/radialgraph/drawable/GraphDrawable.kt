package com.duartbreedt.radialgraph.drawable

import android.graphics.DashPathEffect
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.RectF
import android.graphics.drawable.Drawable
import com.duartbreedt.radialgraph.model.GraphConfig
import com.duartbreedt.radialgraph.model.SectionState

abstract class GraphDrawable(
    open val graphConfig: GraphConfig,
    open val sectionStates: List<SectionState>
) : Drawable() {

    private val width = 40f
    private val startingRotation = -90f
    protected var pathLength: Float = 0f

    /**
     * Creates a paint object with a phase value to indicate the progress of the bar
     *
     * @param phase The progress of the bar where 0f is a full bar and the path's length is an empty bar
     */
    protected fun buildPhasedPathPaint(progress: Float, resolvedColor: Int): Paint {

        val phase =
            if (graphConfig.isClockwise()) pathLength + progress
            else pathLength - progress

        return Paint().apply {
            strokeWidth = width
            color = resolvedColor
            pathEffect = DashPathEffect(floatArrayOf(pathLength, pathLength), phase)
            style = Paint.Style.STROKE
            flags = Paint.ANTI_ALIAS_FLAG
            strokeCap = Paint.Cap.BUTT
        }
    }

    protected fun buildCircularPath(boundaries: RectF): Path {
        val path = Path()

        path.addArc(boundaries, 0f, -360f)

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

    protected fun calculateBoundaries(): RectF {
        return RectF(
            bounds.left.toFloat() + (width / 2f),
            bounds.top.toFloat() + (width / 2f),
            bounds.right.toFloat() - (width / 2f),
            bounds.bottom.toFloat() - (width / 2f)
        )
    }

    override fun getOpacity(): Int = PixelFormat.OPAQUE
}