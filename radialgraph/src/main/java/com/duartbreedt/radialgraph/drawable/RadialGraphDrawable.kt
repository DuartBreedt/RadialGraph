package com.duartbreedt.radialgraph.drawable

import android.animation.ObjectAnimator
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PathMeasure
import android.os.Build
import android.util.FloatProperty
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.RequiresApi
import com.duartbreedt.radialgraph.model.GraphConfig
import com.duartbreedt.radialgraph.model.GraphNode
import com.duartbreedt.radialgraph.model.SectionState

class RadialGraphDrawable(
    override val graphConfig: GraphConfig,
    override val sectionStates: List<SectionState>
) : GraphDrawable(graphConfig, sectionStates) {

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

            if (graphConfig.graphNodeType != GraphNode.NONE) {
                if (sectionState.isLastSection) {
                    val pathMeasure = PathMeasure(sectionState.path!!, false)
                    val coordinates = FloatArray(2)

                    // Get the position of the end of the last drawn segment
                    pathMeasure.getPosTan(
                        sectionState.length!! * (1 - (sectionState.sweepSize + sectionState.startPosition)),
                        coordinates,
                        null
                    )

                    canvas.drawCircle(
                        coordinates[0],
                        coordinates[1],
                        graphConfig.strokeWidth / 2,
                        buildNodePaint(sectionState, graphConfig.graphNodeColor)
                    )

                    // TODO: Investigate why the magic number 15f works here for centering the text within the circle
                    canvas.drawText(
                        "%",
                        coordinates[0] - 15f,
                        coordinates[1] + 15f,
                        buildNodeTextPaint(sectionState.color, graphConfig.graphNodeTextSize)
                    )
                }
            }
        }
    }

    override fun setAlpha(alpha: Int) {
        // This method is required
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        // This method is required
    }

    fun animateIn() {
        ObjectAnimator.ofFloat(
            this,
            PROGRESS, 0f, 1f
        ).apply {
            duration = 1000L
            interpolator = AccelerateDecelerateInterpolator()
        }.start()
    }

    // Creates a progress property to be animated animated
    @RequiresApi(Build.VERSION_CODES.N)
    private object PROGRESS : FloatProperty<RadialGraphDrawable>("progress") {
        override fun setValue(drawable: RadialGraphDrawable, progressPercent: Float) {
            drawable.invalidateSelf()
            for (sectionState in drawable.sectionStates) {
                sectionState.currentProgress = progressPercent * (sectionState.length ?: 0f) *
                    (sectionState.sweepSize + sectionState.startPosition)
            }
        }

        override fun get(drawable: RadialGraphDrawable) = drawable.sectionStates[0].currentProgress
    }
}