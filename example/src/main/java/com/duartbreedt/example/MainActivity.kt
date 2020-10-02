package com.duartbreedt.example

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.duartbreedt.radialgraph.model.Data
import com.duartbreedt.radialgraph.model.GraphNode
import com.duartbreedt.radialgraph.model.Section
import kotlinx.android.synthetic.main.activity_main.*
import java.math.BigDecimal

class MainActivity : AppCompatActivity() {

    private var backgroundTrackState: BackgroundTrackState = BackgroundTrackState.DRAWABLE
    private var graphNodeState: GraphNode = GraphNode.PERCENT
    private var currentDataSet: Int = 0

    private lateinit var data: Data

    private val dataSets = mapOf<String, Pair<BigDecimal, List<Section>>>(
        "Set 1 (Full)" to Pair<BigDecimal, List<Section>>(
            BigDecimal(85), listOf(
                Section(
                    Section.DisplayMode.PERCENT,
                    BigDecimal("15"),
                    Color.parseColor("#CE3E61")
                ),
                Section(
                    Section.DisplayMode.PERCENT,
                    BigDecimal("25"),
                    Color.parseColor("#FB716F")
                ),
                Section(
                    BigDecimal("35"),
                    Color.parseColor("#FEAA85")
                ),
                Section(
                    "STAB",
                    BigDecimal("10"),
                    Color.parseColor("#FDC0A1")
                )
            )
        ),
        "Set 2 (1 small segment)" to Pair<BigDecimal, List<Section>>(
            BigDecimal(100), listOf(
                Section(
                    Section.DisplayMode.PERCENT,
                    BigDecimal("15"),
                    Color.parseColor("#CE3E61")
                )
            )
        ),
        "Set 3 (1 large segment)" to Pair<BigDecimal, List<Section>>(
            BigDecimal(100), listOf(
                Section(
                    Section.DisplayMode.PERCENT,
                    BigDecimal("75"),
                    Color.parseColor("#CE3E61")
                )
            )
        ),
        "Set 4 (Empty)" to Pair<BigDecimal, List<Section>>(
            BigDecimal(100), listOf(
                Section(
                    Section.DisplayMode.PERCENT,
                    BigDecimal(0),
                    Color.parseColor("#CE3E61")
                )
            )
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        selectDataSet()
        updateConfig()
        setOnClickListeners()

        graph_layout.setData(data)

        graph_layout.animateIn()
    }

    private fun setOnClickListeners() {
        btnToggleGraphNode.setOnClickListener { toggleGraphNode() }
        btnToggleBackgroundTrack.setOnClickListener { toggleBackgroundTrack() }
        btnRedraw.setOnClickListener { graph_layout.setData(data) }

        btnAnimateIn.setOnClickListener { graph_layout.animateIn() }
        btnAnimateOut.setOnClickListener { graph_layout.animateOut() }

        btnSwitchDataSet.setOnClickListener { switchDataSet() }
    }

    private fun toggleGraphNode() {
        val newState = if (graphNodeState == GraphNode.PERCENT) GraphNode.NONE else GraphNode.PERCENT
        graphNodeState = newState

        graph_layout.setGraphNode(newState)

        updateConfig()
    }

    private fun toggleBackgroundTrack() {
        val currentOrdinal = backgroundTrackState.ordinal
        val newOrdinal = if (currentOrdinal == BackgroundTrackState.values().lastIndex) 0 else currentOrdinal + 1

        backgroundTrackState = BackgroundTrackState.values()[newOrdinal]

        when (backgroundTrackState) {
            BackgroundTrackState.OFF -> graph_layout.removeBackgroundTrack()
            BackgroundTrackState.COLOR -> graph_layout.setBackgroundTrack(ContextCompat.getColor(this, R.color.black))
            BackgroundTrackState.DRAWABLE -> graph_layout.setBackgroundTrack(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.bg_graph_track
                )
            )
        }

        updateConfig()
    }

    private fun switchDataSet() {
        val newDataSet = if (currentDataSet == dataSets.keys.size - 1) 0 else currentDataSet + 1
        currentDataSet = newDataSet

        selectDataSet()

        updateConfig()
    }

    private fun selectDataSet() {
        val sections: List<Section> = dataSets[dataSets.keys.elementAt(currentDataSet)]!!.second
        val total: BigDecimal = dataSets[dataSets.keys.elementAt(currentDataSet)]!!.first

        data = Data(sections, total)
    }

    private fun updateConfig() {
        val stringBuilder = StringBuilder("Current configuration:\n\n")

        stringBuilder.append("Data Set: ${dataSets.keys.elementAt(currentDataSet)}\n")
        stringBuilder.append("Graph Node: ${graphNodeState.name}\n")
        stringBuilder.append("Background Track: ${backgroundTrackState.name}")

        currentConfig.text = stringBuilder.toString()
    }

    enum class BackgroundTrackState {
        OFF,
        COLOR,
        DRAWABLE
    }
}
