package com.duartbreedt.radialgraph.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.TypedValue
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.ViewCompat
import com.duartbreedt.radialgraph.R
import kotlin.math.cos
import kotlin.math.sin

@SuppressLint("ViewConstructor")
internal class LabelView(context: Context, value: String, @ColorInt val color: Int, posValue: Float) :
    AppCompatTextView(context, null) {

    private var positionValue: Float = posValue

    init {
        id = ViewCompat.generateViewId()
        text = value
        setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.label_defaultTextSize))
        setTextColor(color)
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    fun setPosition(radius: Float) {
        val xGraphCenter = x
        val yGraphCenter = y

        val labelPadding = context.resources.getDimension(R.dimen.label_padding)
        val labelDistanceFromGraph = radius + labelPadding + (width / 2f * (cosineFunction(positionValue)))

        val xLabelPosition = x
        val yLabelPosition = y - labelDistanceFromGraph

        val angleOfRotation = -positionValue * 2 * Math.PI

        val xVector = xLabelPosition - xGraphCenter
        val yVector = yLabelPosition - yGraphCenter + (height / 2)

        // Rotate label about the center of the graph
        y = ((xVector * sin(angleOfRotation) + yVector * cos(angleOfRotation)) + yGraphCenter).toFloat()
        x = ((xVector * cos(angleOfRotation) - yVector * sin(angleOfRotation)) + xGraphCenter).toFloat()
    }

    /**
     * Intended to provide values which shift text labels away from the graph when they are along side the graph
     * and bring the labels nearer the graph when they are above or below the graph
     *
     * @param x A cycle completes for every 0.5 increment of x
     * @return value which fluctuates between [0.0, 1.0]
     */
    private fun cosineFunction(x: Float): Float {
        val amplitude = 0.5f
        val period = 4f
        val midLine = 0.5f
        return (amplitude * cos(period * Math.PI * x + Math.PI) + midLine).toFloat()
    }
}