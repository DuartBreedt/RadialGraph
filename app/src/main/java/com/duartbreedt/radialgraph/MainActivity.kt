package com.duartbreedt.radialgraph

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.duartbreedt.radialgraph.model.GraphCategory
import com.duartbreedt.radialgraph.model.GraphData
import kotlinx.android.synthetic.main.activity_main.*
import java.math.BigDecimal

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

            val graphCategories = mutableListOf(
                GraphCategory(
                    "A",
                    BigDecimal("5"),
                    R.color.red
                ),
                GraphCategory(
                    "B",
                    BigDecimal("10"),
                    R.color.green

                ),
                GraphCategory(
                    BigDecimal("15"),
                    R.color.blue
                )
            )

            val graphData = GraphData(graphCategories)

            graph_layout.draw(graphData)
    }
}
