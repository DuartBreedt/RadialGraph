package com.duartbreedt.radialgraph.drawable

import android.animation.ObjectAnimator
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PathMeasure
import android.os.Build
import android.util.FloatProperty
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.RequiresApi
import com.duartbreedt.radialgraph.model.Data
import com.duartbreedt.radialgraph.model.GraphConfig
import com.duartbreedt.radialgraph.model.SectionState

class RadialGraphDrawable(
    override val graphConfig: GraphConfig,
    override val sectionStates: List<SectionState>
) : GraphDrawable(graphConfig, sectionStates) {

    override fun draw(canvas: Canvas) {
        val boundaries = calculateBoundaries()

        for (sectionState in sectionStates) {
            val path = buildCircularPath(boundaries)

            // FIXME: Potentially move these out of the draw function
            pathLength = PathMeasure(path, false).length

            val paint = buildPhasedPathPaint(sectionState)

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

                sectionState.currentProgress = progressPercent * drawable.pathLength * (sectionState.sweepSize +
                    sectionState.startPosition)
            }
        }

        override fun get(drawable: RadialGraphDrawable) = drawable.sectionStates[0].currentProgress
    }
}