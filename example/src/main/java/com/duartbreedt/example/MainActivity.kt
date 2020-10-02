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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        val sections = mutableListOf(
            Section(
                Section.DisplayMode.PERCENT,
                BigDecimal("15"),
                Color.parseColor("#CE3E61")
            ),
            Section(
                Section.DisplayMode.VALUE,
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

        val data = Data(sections, BigDecimal("100"))

        btnToggle.setOnClickListener {
            //graph_layout.setGraphNode(GraphNode.NONE)
            graph_layout.removeBackgroundTrack()
            graph_layout.redraw(data)
        }

        graph_layout.setData(data)

        graph_layout.animateIn()
    }
}
