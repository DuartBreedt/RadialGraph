package com.duartbreedt.radialgraph.drawable

import android.animation.ObjectAnimator
import android.graphics.*
import android.util.Log
import android.util.Property
import android.view.animation.AccelerateDecelerateInterpolator
import com.duartbreedt.radialgraph.drawable.node.*
import com.duartbreedt.radialgraph.model.*

class RadialGraphDrawable(
    override val graphConfig: GraphConfig,
    override val sectionStates: List<SectionState>
) : GraphDrawable(graphConfig, sectionStates) {

    companion object {
        private val TAG = RadialGraphDrawable::class.simpleName

        private const val endGradientCapFill = 0.98f
    }

    private val lastSectionState = sectionStates.first { it.isLastSection }
    private var node: Node? = null

    //region Public API
    override fun draw(canvas: Canvas) {
        initializeDrawable()

        for (sectionState in sectionStates) {
            updatePhasedPathPaint(sectionState)
            canvas.drawPath(sectionState.path!!, sectionState.paint!!)
        }

        if (node == null) {
            initializeGraphNode()
        }

        node?.updateNode(canvas)
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
        if (state.color.size < 2) {
            return
        }
        
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

    private fun initializeGraphNode() {
        if (graphConfig.graphNodeType == GraphNode.NONE && graphConfig.capStyle != Cap.ROUND) {
            return
        }

        node = CircularNode(lastSectionState, lastSectionState.color.last(), graphConfig)

        if (graphConfig.graphNodeType != GraphNode.NONE) {

            node = PaddedCircularNodeDecorator(node!!, graphConfig)

            when (graphConfig.graphNodeType) {
                GraphNode.PERCENT -> node = TextNodeDecorator(lastSectionState.color.last(), '%', node!!, graphConfig)
                GraphNode.ICON -> node = IconNodeDecorator(lastSectionState.color.last(), node!!, graphConfig)
                else -> Log.e(TAG, "${graphConfig.graphNodeType} has not yet been catered for!")
            }
        }
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