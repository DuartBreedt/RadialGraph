package com.duartbreedt.radialgraph.drawable

import android.animation.ObjectAnimator
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PathMeasure
import android.os.Build
import android.util.FloatProperty
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.RequiresApi
import com.duartbreedt.radialgraph.model.SectionState

class RadialGraphDrawable(override var sectionStates: List<SectionState>) : GraphDrawable(sectionStates) {

    override fun draw(canvas: Canvas) {
        val boundaries = calculateBoundaries()

        for (graph in sectionStates) {
            val path = buildCircularPath(boundaries)
            pathLength = PathMeasure(path, false).length
            val paint = buildPhasedPathPaint(pathLength - graph.currentProgress, graph.color)

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
        ObjectAnimator.ofFloat(this,
            PROGRESS, 0f, 1f).apply {
            duration = 1000L
            interpolator = AccelerateDecelerateInterpolator()
        }.start()
    }

    // Creates a progress property to be animated animated
    @RequiresApi(Build.VERSION_CODES.N)
    private object PROGRESS : FloatProperty<RadialGraphDrawable>("progress") {
        override fun setValue(drawable: RadialGraphDrawable, progressPercent: Float) {
            drawable.invalidateSelf()
            var portionStartPosition = 1f
            for (graphValue in drawable.sectionStates) {
                graphValue.currentProgress = drawable.pathLength * progressPercent * portionStartPosition
                portionStartPosition -= graphValue.value
            }
        }

        override fun get(drawable: RadialGraphDrawable) = drawable.sectionStates[0].currentProgress
    }
}