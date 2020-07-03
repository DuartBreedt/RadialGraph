package com.duartbreedt.radialgraph

import android.graphics.DashPathEffect
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.RectF
import android.graphics.drawable.Drawable

abstract class ChartDrawable(open var graphValues: List<GraphValue>) : Drawable() {
    private val strokeWidth = 40f
    private val startingRotation = -90f
    protected var pathLength: Float = 0f

    /**
     * Creates a paint object with a phase value to indicate the progress of the bar
     *
     * @param phase The progress of the bar where 0f is a full bar and the path's length is an empty bar
     */
    protected fun buildPhasedPathPaint(phase: Float, color: Int): Paint {
        val paint = Paint()
        paint.strokeWidth = strokeWidth
        paint.color = color
        paint.pathEffect = DashPathEffect(floatArrayOf(pathLength, pathLength), phase)
        paint.style = Paint.Style.STROKE
        paint.flags = Paint.ANTI_ALIAS_FLAG
        paint.strokeCap = Paint.Cap.BUTT
        return paint
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
            bounds.left.toFloat() + (strokeWidth / 2f),
            bounds.top.toFloat() + (strokeWidth / 2f),
            bounds.right.toFloat() - (strokeWidth / 2f),
            bounds.bottom.toFloat() - (strokeWidth / 2f)
        )
    }

    override fun getOpacity(): Int = PixelFormat.OPAQUE

    data class GraphValue(var value: Float, var color: Int, var progress: Float = 0f)
}