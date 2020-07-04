package com.duartbreedt.radialgraph

import android.animation.ObjectAnimator
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PathMeasure
import android.os.Build
import android.util.FloatProperty
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.RequiresApi

class RadialGraphDrawable(override var graphValues: List<GraphValue>) : GraphDrawable(graphValues) {

    override fun draw(canvas: Canvas) {
        val boundaries = calculateBoundaries()

        for (graph in graphValues) {
            val path = buildCircularPath(boundaries)
            pathLength = PathMeasure(path, false).length
            val paint = buildPhasedPathPaint(pathLength - graph.progress, graph.color)

            canvas.drawPath(path, paint)
        }
    }

    override fun setAlpha(alpha: Int) {
        // This method is required
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        // This method is required
    }

    fun animateIn() {
        ObjectAnimator.ofFloat(this, PROGRESS, 0f, 1f).apply {
            duration = 1000L
            interpolator = AccelerateDecelerateInterpolator()
        }.start()
    }

    // Creates a progress property to be animated animated
    @RequiresApi(Build.VERSION_CODES.N)
    private object PROGRESS : FloatProperty<RadialGraphDrawable>("progress") {
        override fun setValue(drawable: RadialGraphDrawable, progressPercent: Float) {
            drawable.invalidateSelf()
            var graphValueSum = drawable.graphValues.sumByDouble { graphValue -> graphValue.value.toDouble() }.toFloat()
            for (graphValue in drawable.graphValues) {
                graphValue.progress = drawable.pathLength * progressPercent * graphValueSum
                graphValueSum -= graphValue.value
            }
        }

        override fun get(drawable: RadialGraphDrawable) = drawable.graphValues[0].progress
    }
}