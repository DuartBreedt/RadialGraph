package com.duartbreedt.radialgraph.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.ViewCompat
import com.duartbreedt.radialgraph.model.GraphData
import com.duartbreedt.radialgraph.R
import com.duartbreedt.radialgraph.drawable.RadialGraphDrawable
import java.math.BigDecimal

class RadialGraph(context: Context, @Nullable attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    //region Properties
    private var graphView: AppCompatImageView? = null
    private val labelViews: MutableList<LabelView> = mutableListOf()
    //endregion

    //region Android Lifecycle
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        labelViews.forEach { it.setPosition(graphView!!.height / 2f) }
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
        removeGraphView()

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

    private fun removeGraphView() {
        if (graphView != null) {
            removeView(graphView)
            graphView = null
        }
    }

    private fun drawGraph(graphData: GraphData) {
        val graph =
            RadialGraphDrawable(graphData.categories.map {
                it.toGraphValue(context)
            })

        graphView!!.setImageDrawable(graph)

        graph.animateIn()
    }

    private fun addLabelViewsToLayout(graphData: GraphData) {
        removeAllLabels()

        var labelStartPositionValue = BigDecimal.ONE

        for (category in graphData.categories) {
            context?.let { context ->
                val categoryPortion: BigDecimal = category.normalizedValue
                val labelPositionValue: Float = category.calculateLabelPositionValue(labelStartPositionValue)
                val labelView =
                    LabelView(context, category, labelPositionValue)

                labelViews.add(labelView)
                addView(labelView)
                setConstraints(labelView)

                labelStartPositionValue = labelStartPositionValue.minus(categoryPortion)
            }
        }
    }

    private fun removeAllLabels() {
        labelViews.forEach { removeView(it) }
        labelViews.clear()
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