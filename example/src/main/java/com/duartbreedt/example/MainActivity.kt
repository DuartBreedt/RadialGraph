package com.duartbreedt.example

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.duartbreedt.radialgraph.model.Data
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
            // Section(
            //     Section.DisplayMode.VALUE,
            //     BigDecimal("30"),
            //     ContextCompat.getColor(this, R.color.blue)
            // ),
            // Section(
            //     BigDecimal("10"),
            //     ContextCompat.getColor(this, R.color.yellow)
            // ),
            // Section(
            //     "STAB",
            //     BigDecimal("10"),
            //     ContextCompat.getColor(this, R.color.green)
            // )
        )

        graph_layout.draw(Data(sections, BigDecimal("60")))
    }
}
