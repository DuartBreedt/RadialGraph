package com.duartbreedt.radialgraph

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.math.BigDecimal

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()


            val chartCategories = mutableListOf(
                ChartCategory(
                    "A",
                    BigDecimal("5"),
                    R.color.red
                ),
                ChartCategory(
                    "B",
                    BigDecimal("10"),
                    R.color.green

                ),
                ChartCategory(
                    BigDecimal("15"),
                    R.color.blue
                )
            )

            val chartData = ChartData(chartCategories)

            overview_pie_chart_layout.drawChartPercent(chartData)
    }
}
