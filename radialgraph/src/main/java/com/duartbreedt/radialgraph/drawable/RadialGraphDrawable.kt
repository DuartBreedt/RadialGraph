package com.duartbreedt.radialgraph.drawable

import android.animation.ObjectAnimator
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PathMeasure
import android.graphics.Rect
import android.util.Log
import android.util.Property
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.graphics.drawable.toBitmap
import com.duartbreedt.radialgraph.model.Cap
import com.duartbreedt.radialgraph.model.GraphConfig
import com.duartbreedt.radialgraph.model.GraphNode
import com.duartbreedt.radialgraph.model.SectionState

class RadialGraphDrawable(
    override val graphConfig: GraphConfig,
    override val sectionStates: List<SectionState>
) : GraphDrawable(graphConfig, sectionStates) {

    companion object {
        private val TAG = RadialGraphDrawable::class.simpleName

        private const val CAP_STYLE_BUTT = 0
    }

    //region Public API
    override fun draw(canvas: Canvas) {
        val boundaries = calculateBoundaries()

        for (sectionState in sectionStates) {
            if (sectionState.path == null) {
                sectionState.path = buildCircularPath(boundaries)
            }

            if (sectionState.length == null) {
                sectionState.length = PathMeasure(sectionState.path, false).length
            }

            sectionState.paint = buildPhasedPathPaint(sectionState)
            canvas.drawPath(sectionState.path!!, sectionState.paint!!)
        }

        drawGraphNode(canvas)
    }

    override fun setAlpha(alpha: Int) {
        // This method is required
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        // This method is required
    }

    fun setAnimationProgress(progress: Float) {
        FillProgress.set(this, progress)
    }

    fun isAnimationComplete(): Boolean =
        FillProgress.get(this) == 1f

    fun animate(from: Float, to: Float) {
        ObjectAnimator.ofFloat(this, FillProgress, from, to).apply {
            duration = graphConfig.animationDuration
            interpolator = AccelerateDecelerateInterpolator()
        }.start()
    }
    //endregion

    //region Helper Functions
    private fun drawGraphNode(canvas: Canvas) {
        val lastSectionState = sectionStates.first { it.isLastSection }
        val graphEndCoords = FloatArray(2)
        val strokeRadius = graphConfig.strokeWidth / 2
        val innerCircleRadius = strokeRadius - (graphConfig.strokeWidth / 10)

        // Get the position of the end of the last drawn segment
        PathMeasure(lastSectionState.path!!, false).getPosTan(
            lastSectionState.length!! - lastSectionState.currentProgress + 1.0f,
            graphEndCoords,
            null
        )

        // Add a circle with the same background as the last segment drawn
        if (graphConfig.graphNodeType != GraphNode.NONE || graphConfig.capStyle == Cap.ROUND) {
            canvas.drawCircle(
                graphEndCoords[0],
                graphEndCoords[1],
                strokeRadius,
                buildFillPaint(lastSectionState.color)
            )
        }

        if (graphConfig.graphNodeType != GraphNode.NONE) {

            // Draw inner coloured node circle
            canvas.drawCircle(
                graphEndCoords[0],
                graphEndCoords[1],
                innerCircleRadius,
                buildFillPaint(graphConfig.graphNodeColor)
            )

            when (graphConfig.graphNodeType) {
                GraphNode.PERCENT -> drawTextNode(canvas, graphEndCoords, lastSectionState, '%')
                GraphNode.ICON -> drawIconNode(canvas, graphEndCoords, lastSectionState, innerCircleRadius)
                else -> Log.e(TAG, "${graphConfig.graphNodeType} has not yet been catered for!")
            }
        }
    }

    private fun drawIconNode(
        canvas: Canvas,
        graphEndCoords: FloatArray,
        lastSectionState: SectionState,
        innerCircleRadius: Float
    ) {
        graphConfig.graphNodeIcon?.setTint(lastSectionState.color)
        graphConfig.graphNodeIcon?.toBitmap()?.let {

            // Ensures we retains the icon aspect ratio
            val widthMultiplier = if (it.width > it.height) 1.0f else it.width.toFloat() / it.height
            val heightMultiplier = if (it.width > it.height) it.height.toFloat() / it.width else 1.0f

            val iconSize: Float = (innerCircleRadius * 2f) - (innerCircleRadius * 0.6f)
            canvas.drawBitmap(
                Bitmap.createScaledBitmap(
                    it,
                    (iconSize * widthMultiplier).toInt(),
                    (iconSize * heightMultiplier).toInt(),
                    true
                ),
                graphEndCoords[0] - (iconSize / 2f),
                graphEndCoords[1] - (iconSize / 2f),
                null
            )
        }
    }

    private fun drawTextNode(canvas: Canvas, graphEndCoords: FloatArray, lastSectionState: SectionState, node: Char) {
        val nodeBounds = Rect()
        val nodePaint = buildNodeTextPaint(lastSectionState.color, graphConfig.graphNodeTextSize).apply {
            getTextBounds(node.toString(), 0, node.toString().length, nodeBounds)
        }
        val nodeOffset: Int = nodeBounds.width() / 2

        canvas.drawText(
            node.toString(),
            graphEndCoords[0] - nodeOffset,
            graphEndCoords[1] + nodeOffset,
            nodePaint
        )
    }

    // Creates a progress property to be animated animated
    private object FillProgress : Property<RadialGraphDrawable, Float>(Float::class.java, "progress") {
        override fun set(drawable: RadialGraphDrawable, progressPercent: Float) {
            drawable.invalidateSelf()
            for (sectionState in drawable.sectionStates) {
                sectionState.currentProgress = progressPercent * (sectionState.length ?: 0f) *
                    (sectionState.sweepSize + sectionState.startPosition)
            }
        }

        override fun get(drawable: RadialGraphDrawable) = drawable.sectionStates[0].currentProgress
    }
    //endregion
}