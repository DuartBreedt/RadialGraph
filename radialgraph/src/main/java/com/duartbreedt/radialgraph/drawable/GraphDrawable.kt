package com.duartbreedt.radialgraph.drawable

import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.PixelFormat
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.Log
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
     * @param state The state for which we are creating a paint object
     */
    protected fun buildPhasedPathPaint(state: SectionState) {
        val phase: Float = state.length!! + state.currentProgress

        state.paint = Paint().apply {
            strokeWidth = graphConfig.strokeWidth
            color = state.color.first()
            pathEffect = DashPathEffect(floatArrayOf(state.length!!, state.length!!), phase)
            style = Paint.Style.STROKE
            flags = Paint.ANTI_ALIAS_FLAG
            strokeCap = graphConfig.capStyle.paintCapStyle
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

    protected fun updateSegmentedGradientPath(state: SectionState, nextSectionState: SectionState?) {
        val measure = PathMeasure(state.path!!, false)
        val startCoords = FloatArray(2)
        val endCoords = FloatArray(2)

        measure.getPosTan(state.length!! - (nextSectionState?.currentProgress ?: 0f), startCoords, null)
        measure.getPosTan(state.length!! - state.currentProgress, endCoords, null)

        state.paint?.shader = LinearGradient(
            startCoords[0],
            startCoords[1],
            endCoords[0],
            endCoords[1],
            state.color.toIntArray(),
            null,
            Shader.TileMode.CLAMP
        )
    }

    protected fun buildCircularPath(boundaries: RectF): Path {
        val path = Path()
        val sign = if (graphConfig.animationDirection == AnimationDirection.CLOCKWISE) -1 else 1
        path.addArc(boundaries, 0f, 360f * sign)

        // The starting position of the arc is undesirable, therefore set it explicitly
        rotatePath(path, startingRotation)
        return path
    }

    protected fun buildFillPaint(colorInt: Int) = Paint().apply {
        color = colorInt
        style = Paint.Style.FILL
        flags = Paint.ANTI_ALIAS_FLAG
    }

    protected fun buildNodeTextPaint(textColor: Int, textSize: Float): Paint = TextPaint().apply {
        this.textSize = textSize
        color = textColor
        flags = Paint.ANTI_ALIAS_FLAG
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