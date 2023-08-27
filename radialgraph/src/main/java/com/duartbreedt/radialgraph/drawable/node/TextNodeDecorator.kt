package com.duartbreedt.radialgraph.drawable.node

import android.graphics.Canvas
import android.graphics.Rect
import androidx.annotation.ColorInt
import com.duartbreedt.radialgraph.TextPaintProvider
import com.duartbreedt.radialgraph.model.GraphConfig

class TextNodeDecorator(@ColorInt val color: Int, private val char: Char, node: Node, graphConfig: GraphConfig) :
    BaseNodeDecorator(node, graphConfig) {

    private val nodeBounds = Rect()
    private val nodePaint = TextPaintProvider.getPaint(color, graphConfig.graphNodeTextSize).apply {
        getTextBounds(char.toString(), 0, char.toString().length, nodeBounds)
    }
    private val nodeOffset: Int = nodeBounds.width() / 2

    override fun updateNode(canvas: Canvas) {
        node.updateNode(canvas)

        canvas.drawText(
            char.toString(),
            getNodePosition()[0] - nodeOffset,
            getNodePosition()[1] + nodeOffset,
            nodePaint
        )
    }
}