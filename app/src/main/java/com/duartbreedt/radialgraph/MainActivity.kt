package com.duartbreedt.radialgraph

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.duartbreedt.radialgraph.model.Section
import com.duartbreedt.radialgraph.model.Data
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
                    "A",
                    BigDecimal("5"),
                    R.color.red
                ),
                Section(
                    "B",
                    BigDecimal("10"),
                    R.color.green

                ),
                Section(
                    BigDecimal("15"),
                    R.color.blue
                )
            )

            graph_layout.draw(Data(sections))
    }
}
