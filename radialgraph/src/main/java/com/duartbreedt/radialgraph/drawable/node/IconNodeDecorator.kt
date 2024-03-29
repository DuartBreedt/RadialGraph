package com.duartbreedt.radialgraph.drawable.node

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.toBitmap
import com.duartbreedt.radialgraph.model.GraphConfig

internal class IconNodeDecorator(@ColorInt val color: Int, node: Node, graphConfig: GraphConfig) : BaseNodeDecorator(node, graphConfig) {

    private var drawableHashCode: Int = 0
    private var nodeIconBitmap: Bitmap? = null
    private var nodeIconBitmapWidthMultiplier: Float? = null
    private var nodeIconBitmapHeightMultiplier: Float? = null

    private val iconSize: Float = (super.getRadius() * 2f) - (super.getRadius() * 0.6f)

    override fun updateNode(canvas: Canvas) {
        super.updateNode(canvas)

        if (graphConfig.graphNodeIcon != null && drawableHashCode != graphConfig.graphNodeIcon.hashCode()) {

            graphConfig.graphNodeIcon?.setTint(color)
            nodeIconBitmap = graphConfig.graphNodeIcon?.toBitmap()

            // Ensures we retains the icon aspect ratio
            nodeIconBitmap?.let {
                nodeIconBitmapWidthMultiplier = if (it.width > it.height) 1.0f else it.width.toFloat() / it.height
                nodeIconBitmapHeightMultiplier = if (it.width > it.height) it.height.toFloat() / it.width else 1.0f
            }

            drawableHashCode = graphConfig.graphNodeIcon.hashCode()
        }

        if (nodeIconBitmap != null) {
            canvas.drawBitmap(
                Bitmap.createScaledBitmap(
                    nodeIconBitmap!!,
                    (iconSize * nodeIconBitmapWidthMultiplier!!).toInt(),
                    (iconSize * nodeIconBitmapHeightMultiplier!!).toInt(),
                    true
                ),
                getNodePosition()[0] - (iconSize / 2f),
                getNodePosition()[1] - (iconSize / 2f),
                null
            )
        }
    }
}