package com.duartbreedt.radialgraph.drawable

import android.animation.ObjectAnimator
import android.graphics.*
import android.util.Log
import android.util.Property
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.toBitmap
import com.duartbreedt.radialgraph.PaintProvider
import com.duartbreedt.radialgraph.TextPaintProvider
import com.duartbreedt.radialgraph.model.*

class RadialGraphDrawable(
    override val graphConfig: GraphConfig,
    override val sectionStates: List<SectionState>
) : GraphDrawable(graphConfig, sectionStates) {

    companion object {
        private val TAG = RadialGraphDrawable::class.simpleName

        private const val endGradientCapFill = 0.98f
    }

    private val strokeRadius = graphConfig.strokeWidth / 2
    private val innerCircleRadius = strokeRadius - (graphConfig.strokeWidth / 10)
    private val lastSectionState = sectionStates.first { it.isLastSection }

    //region Public API
    override fun draw(canvas: Canvas) {
        initializeDrawable()

        for (sectionState in sectionStates) {
            updatePhasedPathPaint(sectionState)
            canvas.drawPath(sectionState.path!!, sectionState.paint!!)
        }

        // Make this more efficient
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
    // Initialize sections
    private fun initializeDrawable() {
        val boundaries = calculateBoundaries()

        for (sectionState in sectionStates) {
            if (sectionState.path == null) {
                sectionState.path = buildCircularPath(boundaries)
            }

            if (sectionState.length == null) {
                sectionState.length = PathMeasure(sectionState.path, false).length
            }

            if (sectionState.paint == null) {
                buildPhasedPathPaint(sectionState)
                if (graphConfig.isGradientEnabled() && graphConfig.gradientType == GradientType.SWEEP) {
                    applySweepGradient(sectionState)
                }
            }
        }
    }

    private fun applySweepGradient(state: SectionState) {
        val colorList: MutableList<Int> = state.color.toMutableList()
        val positionList: MutableList<Float> = generatePositionList(state, colorList.size).toMutableList()
        val boundaries = calculateBoundaries()

        /*
            Fix for gradient overflow for the first section's first cap when using a cap style other than 'BUTT'.
            This creates a small gradient at the end of the sweep to account for the overflowing cap.
         */
        if (state.startPosition == 0f && graphConfig.capStyle != Cap.BUTT) {
            colorList.add(state.color.first())
            positionList.addAll(
                listOf(
                    positionList.removeLast().coerceAtMost(endGradientCapFill),
                    endGradientCapFill
                )
            )
        }

        state.paint?.shader = SweepGradient(
            boundaries.centerX(),
            boundaries.centerY(),
            colorList.toIntArray(),
            positionList.toFloatArray()
        ).apply {
            setLocalMatrix(Matrix().apply {
                preRotate(
                    startingRotation,
                    boundaries.centerX(),
                    boundaries.centerY()
                )
            })
        }
    }

    private fun generatePositionList(state: SectionState, colorListSize: Int): List<Float> {

        // Distribution of colors on the section as a value between 0.0 and 1.0
        val gradientDistribution = (1f / (colorListSize - 1).coerceAtLeast(1))

        // Gap between each color clamped to the size of the section in relation to the graph
        val clampedGradientGap = when (graphConfig.gradientFill) {
            GradientFill.SECTION -> gradientDistribution * state.sweepSize
            GradientFill.FULL -> gradientDistribution
        }

        val colorPositionIndices: List<Float> = List(colorListSize, Int::toFloat)

        // Generate the position for each color relative to the graph
        return colorPositionIndices.map { (clampedGradientGap * it) + state.startPosition }
    }

    private fun drawGraphNode(canvas: Canvas) {
        val graphEndCoords = FloatArray(2)

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
                PaintProvider.getPaint(lastSectionState.color.last())
            )
        }

        if (graphConfig.graphNodeType != GraphNode.NONE) {

            // Draw inner coloured node circle
            canvas.drawCircle(
                graphEndCoords[0],
                graphEndCoords[1],
                innerCircleRadius,
                PaintProvider.getPaint(graphConfig.graphNodeColor)
            )

            when (graphConfig.graphNodeType) {
                GraphNode.PERCENT -> drawTextNode(canvas, graphEndCoords, lastSectionState.color.last(), '%')
                GraphNode.ICON -> drawIconNode(canvas, graphEndCoords, lastSectionState.color.last(), innerCircleRadius)
                else -> Log.e(TAG, "${graphConfig.graphNodeType} has not yet been catered for!")
            }
        }
    }

    private fun drawIconNode(
        canvas: Canvas,
        graphEndCoords: FloatArray,
        @ColorInt nodeColor: Int,
        innerCircleRadius: Float
    ) {
        graphConfig.graphNodeIcon?.setTint(nodeColor)
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

    private fun drawTextNode(canvas: Canvas, graphEndCoords: FloatArray, @ColorInt nodeColor: Int, node: Char) {
        val nodeBounds = Rect()
        val nodePaint = TextPaintProvider.getPaint(nodeColor, graphConfig.graphNodeTextSize).apply {
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