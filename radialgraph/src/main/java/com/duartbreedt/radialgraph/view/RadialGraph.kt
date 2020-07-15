package com.duartbreedt.radialgraph.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.ConstraintSet.BOTTOM
import androidx.constraintlayout.widget.ConstraintSet.LEFT
import androidx.constraintlayout.widget.ConstraintSet.PARENT_ID
import androidx.constraintlayout.widget.ConstraintSet.RIGHT
import androidx.constraintlayout.widget.ConstraintSet.TOP
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.duartbreedt.radialgraph.R
import com.duartbreedt.radialgraph.drawable.RadialGraphDrawable
import com.duartbreedt.radialgraph.extensions.toFormattedDecimal
import com.duartbreedt.radialgraph.model.AnimationDirection
import com.duartbreedt.radialgraph.model.Cap
import com.duartbreedt.radialgraph.model.Data
import com.duartbreedt.radialgraph.model.GraphConfig
import com.duartbreedt.radialgraph.model.Section
import java.math.BigDecimal
import java.math.RoundingMode

class RadialGraph : ConstraintLayout {

    //region Properties
    private var graphView: AppCompatImageView? = null
    private val labelViews: MutableList<LabelView> = mutableListOf()
    private val graphConfig: GraphConfig
    //endregion

    companion object {
        private const val CLOCKWISE_ANIMATION_DIRECTION = 0
        private const val DEFAULT_ANIMATION_DIRECTION = CLOCKWISE_ANIMATION_DIRECTION
    }

    init {
        clipChildren = false
        clipToPadding = false
    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.RadialGraph)

        val animationDirectionOrdinal: Int =
            attributes.getInt(R.styleable.RadialGraph_animationDirection, DEFAULT_ANIMATION_DIRECTION)
        val animationDirection: AnimationDirection = AnimationDirection.values()[animationDirectionOrdinal]

        val labelsEnabled: Boolean = attributes.getBoolean(R.styleable.RadialGraph_labelsEnabled, false)

        @ColorInt val labelsColor: Int? = if (attributes.hasValue(R.styleable.RadialGraph_labelsColor)) {
            attributes.getColor(
                R.styleable.RadialGraph_labelsColor,
                ContextCompat.getColor(context, R.color.label_defaultColor)
            )
        } else null

        val strokeWidth: Float = attributes.getDimension(R.styleable.RadialGraph_strokeWidth, 0f)

        graphConfig = GraphConfig(
            animationDirection,
            labelsEnabled,
            labelsColor,
            strokeWidth,
            Cap.ROUND
        )

        attributes.recycle()
    }

    //region Android Lifecycle
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        labelViews.forEach { it.setPosition(graphView!!.height / 2f) }
    }
    //endregion

    //region Public API
    fun draw(data: Data) {
        addGraphViewToLayout()
        drawGraph(data)
        if (graphConfig.labelsEnabled) {
            addLabelViewsToLayout(data)
        }
    }
    //endregion

    //region Helper Functions
    private fun addGraphViewToLayout() {
        removeGraphView()

        graphView = AppCompatImageView(context).apply { id = ViewCompat.generateViewId() }

        addView(graphView)

        graphView!!.layoutParams = (graphView!!.layoutParams as LayoutParams).apply {
            width = 0
            height = 0
        }

        setConstraints(graphView)
    }

    private fun removeGraphView() {
        if (graphView != null) {
            removeView(graphView)
            graphView = null
        }
    }

    private fun drawGraph(data: Data) {

        // TODO: Perhaps don't reverse this here and just sort differently a bit higher
        val graph = RadialGraphDrawable(graphConfig, data.toSectionStates().reversed())

        graphView!!.setImageDrawable(graph)

        graph.animateIn()
    }

    private fun addLabelViewsToLayout(data: Data) {
        removeAllLabels()

        var labelStartPositionValue =
            if (graphConfig.isClockwise()) BigDecimal.ONE
            else BigDecimal.ZERO

        // val wot = data.sections.reversed()
        for (section in data.sections) {
            context?.let { context ->

                val sectionNormalizedSize: BigDecimal = section.normalizedValue

                val labelPositionValue: Float = calculateLabelPositionValue(section, labelStartPositionValue)

                val labelValue: String = section.label ?: when (section.displayMode) {
                    Section.DisplayMode.PERCENT ->
                        resources.getString(R.string.label_percentPattern, section.percent.toFormattedDecimal())
                    Section.DisplayMode.VALUE ->
                        section.value.toFormattedDecimal()
                }

                @ColorInt
                val labelColor: Int = graphConfig.labelsColor ?: section.color

                val labelView = LabelView(context, labelValue, labelColor, labelPositionValue)

                labelViews.add(labelView)
                addView(labelView)
                setConstraints(labelView)

                labelStartPositionValue =
                    if (graphConfig.isClockwise()) labelStartPositionValue - sectionNormalizedSize
                    else labelStartPositionValue + sectionNormalizedSize
            }
        }
    }

    private fun calculateLabelPositionValue(section: Section, portionStartPositionValue: BigDecimal): Float {
        val halfSectionSize = section.normalizedValue.divide(BigDecimal("2"), 2, RoundingMode.HALF_EVEN)
        val sectionMidpointPosition =
            if (graphConfig.isClockwise()) (portionStartPositionValue - halfSectionSize)
            else (portionStartPositionValue + halfSectionSize)
        return sectionMidpointPosition.toFloat()
    }

    private fun removeAllLabels() {
        labelViews.forEach { removeView(it) }
        labelViews.clear()
    }

    private fun setConstraints(view: View?) {
        ConstraintSet().apply {
            clone(this@RadialGraph)

            view?.id?.let { viewId ->
                setDimensionRatio(viewId, "1:1")
                connect(viewId, LEFT, PARENT_ID, LEFT)
                connect(viewId, TOP, PARENT_ID, TOP)
                connect(viewId, RIGHT, PARENT_ID, RIGHT)
                connect(viewId, BOTTOM, PARENT_ID, BOTTOM)
            }

            applyTo(this@RadialGraph)
        }
    }
    //endregion
}