package com.duartbreedt.radialgraph.drawable.node

import android.graphics.Canvas
import android.graphics.PathMeasure
import androidx.annotation.ColorInt
import com.duartbreedt.radialgraph.PaintProvider
import com.duartbreedt.radialgraph.model.GraphConfig
import com.duartbreedt.radialgraph.model.SectionState

class CircularNode(private val sectionState: SectionState, @ColorInt val color: Int, graphConfig: GraphConfig) :
    Node(graphConfig) {

    private val nodePosition: FloatArray = FloatArray(2)
    private val nodeRadius = graphConfig.strokeWidth / 2

    override fun updateNode(canvas: Canvas) {
        recalculatePosition()

        // Add a circle with the same background as the last segment drawn
        canvas.drawCircle(
            nodePosition[0],
            nodePosition[1],
            nodeRadius,
            PaintProvider.getPaint(color)
        )
    }

    override fun getRadius(): Float {
        return nodeRadius
    }

    override fun getNodePosition(): FloatArray {
        return nodePosition
    }

    private fun recalculatePosition() {
        // Get the position of the end of the last drawn segment
        PathMeasure(sectionState.path!!, false).getPosTan(
            sectionState.length!! - sectionState.currentProgress + 1.0f,
            nodePosition,
            null
        )
    }
}