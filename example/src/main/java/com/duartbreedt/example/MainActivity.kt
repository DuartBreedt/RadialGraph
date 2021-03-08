package com.duartbreedt.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.duartbreedt.example.DataSets.dataSets
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
        btnToggleLabels.setOnClickListener { toggleLabels() }
    }

    private fun toggleGraphNode() {
        val newState = when (graphNodeState) {
            GraphNode.ICON -> GraphNode.PERCENT
            GraphNode.PERCENT -> GraphNode.NONE
            GraphNode.NONE -> GraphNode.ICON
        }
        graphNodeState = newState

        graph_layout.setGraphNode(newState)

        updateConfig()
    }

    private fun toggleBackgroundTrack() {
        val currentOrdinal = backgroundTrackState.ordinal
        val newOrdinal = if (currentOrdinal == BackgroundTrackState.values().lastIndex) 0 else currentOrdinal + 1

        backgroundTrackState = BackgroundTrackState.values()[newOrdinal]

        when (backgroundTrackState) {
            BackgroundTrackState.OFF -> graph_layout.setBackgroundTrack(newColor = null)
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

    private fun toggleLabels() {
        graph_layout.isLabelsEnabled = !graph_layout.isLabelsEnabled

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
        val stringBuilder = StringBuilder()

        stringBuilder.append("Data Set: ${dataSets.keys.elementAt(currentDataSet)}\n")
        stringBuilder.append("Graph Node: ${graphNodeState.name}\n")
        stringBuilder.append("Background Track: ${backgroundTrackState.name}\n")
        stringBuilder.append("Label Enabled: ${graph_layout.isLabelsEnabled}")

        currentConfig.text = stringBuilder.toString()
    }

    enum class BackgroundTrackState {
        OFF,
        COLOR,
        DRAWABLE
    }
}
