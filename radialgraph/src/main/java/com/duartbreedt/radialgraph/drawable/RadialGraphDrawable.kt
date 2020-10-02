package com.duartbreedt.radialgraph.drawable

import android.animation.ObjectAnimator
import android.content.res.Resources
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
        }

        if (graphConfig.graphNodeType == GraphNode.PERCENT) {
            val sectionState = sectionStates.first { it.isLastSection }
            val coordinates = FloatArray(2)

            // Get the position of the end of the last drawn segment
            PathMeasure(sectionState.path!!, false).getPosTan(
                sectionState.length!! - sectionState.currentProgress,
                coordinates,
                null
            )

            // Add a circle with the same background as the last segment drawn
            canvas.drawCircle(
                coordinates[0],
                coordinates[1],
                graphConfig.strokeWidth / 2,
                buildFillPaint(sectionState.color)
            )

            // Draw coloured node circle
            canvas.drawCircle(
                coordinates[0],
                coordinates[1],
                (graphConfig.strokeWidth / 2) - (graphConfig.strokeWidth / 10),
                buildFillPaint(graphConfig.graphNodeColor)
            )

            val textCenterOffset: Float = graphConfig.graphNodeTextSize / Resources.getSystem().displayMetrics.scaledDensity
            canvas.drawText(
                "%",
                coordinates[0] - textCenterOffset,
                coordinates[1] + textCenterOffset,
                buildNodeTextPaint(sectionState.color, graphConfig.graphNodeTextSize)
            )
        }
    }

    override fun setAlpha(alpha: Int) {
        // This method is required
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        // This method is required
    }

    fun animateIn() {
        ObjectAnimator.ofFloat(this, FillProgress, 0f, 1f).apply {
            duration = graphConfig.animationDuration
            interpolator = AccelerateDecelerateInterpolator()
        }.start()
    }

    // Creates a progress property to be animated animated
    @RequiresApi(Build.VERSION_CODES.N)
    private object FillProgress : FloatProperty<RadialGraphDrawable>("progress") {
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