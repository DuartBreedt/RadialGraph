package com.duartbreedt.radialgraph.drawable

import android.graphics.DashPathEffect
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.PixelFormat
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.text.TextPaint
import com.duartbreedt.radialgraph.model.AnimationDirection
import com.duartbreedt.radialgraph.model.GraphConfig
import com.duartbreedt.radialgraph.model.SectionState

abstract class GraphDrawable(
    open val graphConfig: GraphConfig,
    open val sectionStates: List<SectionState>
) : Drawable() {

    private val startingRotation = -90f

    /**
     * Creates a paint object with a phase value to indicate the progress of the bar
     *
     * @param phase The progress of the bar where 0f is a full bar and the path's length is an empty bar
     */
    protected fun buildPhasedPathPaint(state: SectionState): Paint {
        val phase: Float = state.length!! + state.currentProgress

        return Paint().apply {
            strokeWidth = graphConfig.strokeWidth
            color = state.color
            pathEffect = DashPathEffect(floatArrayOf(state.length!!, state.length!!), phase)
            style = Paint.Style.STROKE
            flags = Paint.ANTI_ALIAS_FLAG
            strokeCap = graphConfig.capStyle.paintCapStyle
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

    protected fun buildNodePath(segmentPath: Path): Path {
        val path = Path()

        val coordinates = FloatArray(2)
        val pathMeasure = PathMeasure(segmentPath, false)

        pathMeasure.getPosTan(pathMeasure.length, coordinates, null)
        path.addCircle(coordinates[0], coordinates[1], 5f, Path.Direction.CW)

        // The starting position of the arc is undesirable, therefore set it explicitly
        rotatePath(path, startingRotation)
        return path
    }

    protected fun buildNodePaint(state: SectionState, colorInt: Int): Paint {
        return Paint().apply {
            strokeWidth = 1f
            color = colorInt
            pathEffect = DashPathEffect(floatArrayOf(state.length!!, state.length!!), 0f)
            style = Paint.Style.FILL
            flags = Paint.ANTI_ALIAS_FLAG
            strokeCap = graphConfig.capStyle.paintCapStyle
        }
    }

    protected fun buildNodeTextPaint(textColor: Int, textSize: Float): Paint {
        return TextPaint().apply {
            this.textSize = textSize
            color = textColor
            flags = Paint.ANTI_ALIAS_FLAG
        }
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
            bounds.left.toFloat() + (graphConfig.strokeWidth / 2f),
            bounds.top.toFloat() + (graphConfig.strokeWidth / 2f),
            bounds.right.toFloat() - (graphConfig.strokeWidth / 2f),
            bounds.bottom.toFloat() - (graphConfig.strokeWidth / 2f)
        )
    }

    override fun getOpacity(): Int = PixelFormat.OPAQUE
}