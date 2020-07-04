package com.duartbreedt.radialgraph

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.ViewCompat
import androidx.core.view.children
import java.math.BigDecimal

class RadialGraph(context: Context, @Nullable attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    //region Properties
    var graphView: AppCompatImageView? = null
    //endregion

    //region Android Lifecycle
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        val graph: AppCompatImageView = children.filter { view -> view is AppCompatImageView }.firstOrNull()
            .let { view -> view as AppCompatImageView }

        val labels: List<LabelView> =
            children.map { view -> if (view is LabelView) view else null }.toList().filterNotNull()

        labels.forEach { it.setPosition(graph.height / 2f) }
    }
    //endregion

    //region Public API
    fun draw(graphData: GraphData) {
        addGraphViewToLayout()
        drawGraph(graphData)
        addLabelViewsToLayout(graphData)
    }
    //endregion

    //region Helper Functions
    private fun addGraphViewToLayout() {
        if (graphView != null) {
            removeView(graphView)
        }

        graphView = AppCompatImageView(context).apply { id = ViewCompat.generateViewId() }
        addView(graphView)

        val margin: Int = resources.getDimensionPixelSize(R.dimen.graph_margin)
        graphView!!.layoutParams = (graphView!!.layoutParams as LayoutParams).apply {
            setMargins(margin, margin, margin, margin)
            width = resources.getDimensionPixelSize(R.dimen.graph_width)
            height = resources.getDimensionPixelSize(R.dimen.graph_height)
        }

        setConstraints(graphView)
    }

    private fun drawGraph(graphData: GraphData) {
        val graph = RadialGraphDrawable(graphData.categories.map { it.toGraphValue(context) })

        graphView!!.setImageDrawable(graph)

        graph.animateIn()
    }

    private fun addLabelViewsToLayout(graphData: GraphData) {
        var labelStartPositionValue = BigDecimal.ONE
        removeAllLabels()

        for (category in graphData.categories) {
            context?.let { context ->
                val categoryPortion: BigDecimal = category.normalizedValue
                val labelPositionValue: Float = category.calculateLabelPositionValue(labelStartPositionValue)
                val labelView = LabelView(context, category, labelPositionValue)

                addView(labelView)
                setConstraints(labelView)

                labelStartPositionValue = labelStartPositionValue.minus(categoryPortion)
            }
        }
    }

    private fun removeAllLabels() {
        children.forEach {
            if (it is LabelView) {
                removeView(it)
            }
        }
    }

    private fun setConstraints(labelView: View?) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(this)
        labelView?.id?.let { labelViewId ->
            constraintSet.setDimensionRatio(labelViewId, "1:1")
            constraintSet.connect(
                labelViewId,
                ConstraintSet.LEFT,
                ConstraintSet.PARENT_ID,
                ConstraintSet.LEFT
            )
            constraintSet.connect(
                labelViewId,
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.TOP
            )
            constraintSet.connect(
                labelViewId,
                ConstraintSet.RIGHT,
                ConstraintSet.PARENT_ID,
                ConstraintSet.RIGHT
            )
            constraintSet.connect(
                labelViewId,
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM
            )
        }

        constraintSet.applyTo(this)
    }
//endregion
}