package com.duartbreedt.radialgraph

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.children
import java.math.BigDecimal

class LabeledPieChartLayout(context: Context, @Nullable attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        val graph: AppCompatImageView = children.filter { view -> view is AppCompatImageView }.firstOrNull()
            .let { view -> view as AppCompatImageView }

        val labels: List<LabelView> =
            children.map { view -> if (view is LabelView) view else null }.toList().filterNotNull()

        labels.forEach { it.setPosition(graph.height / 2f) }
    }

    fun drawChartPercent(chartData: ChartData) {
        drawChart(chartData)
        addLabelViewsToLayout(chartData)
    }

    private fun drawChart(chartData: ChartData) {

        val chartPortions = ArrayList<ChartDrawable.GraphValue>()

        for (category in chartData.categories) {
            chartPortions.add(getChartPortion(category))
        }

        val portfolioPieChart = PieChartDrawable(chartPortions)

        (children.first { child -> child is AppCompatImageView } as AppCompatImageView)
            .setImageDrawable(portfolioPieChart)

        portfolioPieChart.animateIn()
    }

    private fun addLabelViewsToLayout(chartData: ChartData) {
        var labelStartPositionValue = BigDecimal.ONE
        removeAllLabels()

        for (category in chartData.categories) {
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
        val labelsToRemove = mutableListOf<LabelView>()

        children.forEach { view ->
            if (view is LabelView) {
                labelsToRemove.add(view)
            }
        }

        labelsToRemove.forEach { removeView(it) }
    }

    private fun setConstraints(labelView: LabelView?) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(this)
        labelView?.id?.let { labelViewId ->
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

    private fun getChartPortion(category: ChartCategory): ChartDrawable.GraphValue {
        val graphColor = ContextCompat.getColor(context, category.color)

        return if (category.value == BigDecimal.ZERO) ChartDrawable.GraphValue(0F, graphColor)
        else ChartDrawable.GraphValue(category.normalizedValue.toFloat(), graphColor)
    }
}