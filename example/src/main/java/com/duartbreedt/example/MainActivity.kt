package com.duartbreedt.example

import android.app.Activity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.duartbreedt.example.DataSets.dataSets
import com.duartbreedt.example.databinding.ActivityMainBinding
import com.duartbreedt.radialgraph.model.Data
import com.duartbreedt.radialgraph.model.GraphNode
import com.duartbreedt.radialgraph.model.Section
import java.math.BigDecimal

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding

    private var backgroundTrackState: BackgroundTrackState = BackgroundTrackState.DRAWABLE
    private var graphNodeState: GraphNode = GraphNode.PERCENT
    private var currentDataSet: Int = 0

    private lateinit var data: Data

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        selectDataSet()
        updateConfig()
        setOnClickListeners()

        binding.graphLayout.setData(data)

        binding.graphLayout.animateIn()
    }

    private fun setOnClickListeners() {
        binding.btnToggleGraphNode.setOnClickListener { toggleGraphNode() }
        binding.btnToggleBackgroundTrack.setOnClickListener { toggleBackgroundTrack() }
        binding.btnRedraw.setOnClickListener { binding.graphLayout.setData(data) }

        binding.btnAnimateIn.setOnClickListener { binding.graphLayout.animateIn() }
        binding.btnAnimateOut.setOnClickListener { binding.graphLayout.animateOut() }

        binding.btnSwitchDataSet.setOnClickListener { switchDataSet() }
        binding.btnToggleLabels.setOnClickListener { toggleLabels() }
    }

    private fun toggleGraphNode() {
        val newState = when (graphNodeState) {
            GraphNode.ICON -> GraphNode.PERCENT
            GraphNode.PERCENT -> GraphNode.NONE
            GraphNode.NONE -> GraphNode.ICON
        }
        graphNodeState = newState

        binding.graphLayout.setGraphNode(newState)

        updateConfig()
    }

    private fun toggleBackgroundTrack() {
        val currentOrdinal = backgroundTrackState.ordinal
        val newOrdinal =
            if (currentOrdinal == BackgroundTrackState.values().lastIndex) 0 else currentOrdinal + 1

        backgroundTrackState = BackgroundTrackState.values()[newOrdinal]

        when (backgroundTrackState) {
            BackgroundTrackState.OFF -> binding.graphLayout.setBackgroundTrack(newColor = null)
            BackgroundTrackState.COLOR -> binding.graphLayout.setBackgroundTrack(
                ContextCompat.getColor(
                    this,
                    R.color.black
                )
            )
            BackgroundTrackState.DRAWABLE -> binding.graphLayout.setBackgroundTrack(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.bg_graph_track
                )
            )
        }

        updateConfig()
    }

    private fun toggleLabels() {
        binding.graphLayout.isLabelsEnabled = !binding.graphLayout.isLabelsEnabled

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
        stringBuilder.append("Label Enabled: ${binding.graphLayout.isLabelsEnabled}")

        binding.currentConfig.text = stringBuilder.toString()
    }

    enum class BackgroundTrackState {
        OFF,
        COLOR,
        DRAWABLE
    }
}
