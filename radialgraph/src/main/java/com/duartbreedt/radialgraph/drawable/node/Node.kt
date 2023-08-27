package com.duartbreedt.radialgraph.drawable.node

import android.graphics.Canvas
import com.duartbreedt.radialgraph.model.GraphConfig

abstract class Node(protected val graphConfig: GraphConfig) {

    abstract fun updateNode(canvas: Canvas)
    abstract fun getRadius(): Float
    abstract fun getNodePosition(): FloatArray
}