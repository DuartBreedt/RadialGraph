package com.duartbreedt.radialgraph.drawable.node

import android.graphics.Canvas
import com.duartbreedt.radialgraph.model.GraphConfig

internal abstract class BaseNodeDecorator(val node: Node, graphConfig: GraphConfig) : Node(graphConfig) {

    override fun updateNode(canvas: Canvas) {
        node.updateNode(canvas)
    }

    override fun getRadius(): Float {
        return node.getRadius()
    }

    override fun getNodePosition(): FloatArray {
        return node.getNodePosition()
    }
}