package com.duartbreedt.radialgraph.drawable.node

import android.graphics.Canvas
import com.duartbreedt.radialgraph.PaintProvider
import com.duartbreedt.radialgraph.model.GraphConfig

internal class PaddedCircularNodeDecorator(node: Node, graphConfig: GraphConfig) : BaseNodeDecorator(node, graphConfig) {

    private val innerCircleRadius = super.getRadius() - (graphConfig.strokeWidth / 10)

    override fun updateNode(canvas: Canvas) {
        node.updateNode(canvas)

        // Draw inner coloured node circle
        canvas.drawCircle(
            getNodePosition()[0],
            getNodePosition()[1],
            innerCircleRadius,
            PaintProvider.getPaint(graphConfig.graphNodeColor)
        )
    }

    override fun getRadius(): Float {
        return innerCircleRadius
    }
}