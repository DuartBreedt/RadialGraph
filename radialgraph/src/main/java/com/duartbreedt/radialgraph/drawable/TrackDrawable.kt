package com.duartbreedt.radialgraph.drawable

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.PixelFormat
import android.graphics.RectF
import android.graphics.drawable.Drawable
import com.duartbreedt.radialgraph.model.GraphConfig

class TrackDrawable(private val graphConfig: GraphConfig, private val backgroundColor: Int) : Drawable() {

    override fun draw(canvas: Canvas) {
        val boundaries = calculateBoundaries()
        val path = Path()

        path.addCircle(boundaries.centerX(), boundaries.centerY(), boundaries.width() / 2, Path.Direction.CW)

        val pathMeasure = PathMeasure(path, false)

        val paint = Paint().apply {
            strokeWidth = graphConfig.strokeWidth
            color = backgroundColor
            pathEffect = DashPathEffect(floatArrayOf(pathMeasure.length, pathMeasure.length), 0f)
            style = Paint.Style.STROKE
            flags = Paint.ANTI_ALIAS_FLAG
            strokeCap = Paint.Cap.BUTT
        }

        canvas.drawPath(path, paint)
    }

    override fun setAlpha(alpha: Int) {
    }

    override fun getOpacity(): Int = PixelFormat.OPAQUE

    override fun setColorFilter(colorFilter: ColorFilter?) {
    }

    private fun calculateBoundaries(): RectF {
        return RectF(
            bounds.left.toFloat() + (graphConfig.strokeWidth / 2f),
            bounds.top.toFloat() + (graphConfig.strokeWidth / 2f),
            bounds.right.toFloat() - (graphConfig.strokeWidth / 2f),
            bounds.bottom.toFloat() - (graphConfig.strokeWidth / 2f)
        )
    }
}