package com.duartbreedt.example

import android.graphics.Color
import com.duartbreedt.radialgraph.model.Section
import java.math.BigDecimal

object DataSets {

    private val fullGraphDataSet = "Full graph" to Pair(
        BigDecimal(85), listOf(
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
                Color.parseColor("#FEAA85"), Color.parseColor("#FB716F")
            ),
            Section(
                "STAB",
                BigDecimal("10"),
                Color.parseColor("#FDC0A1")
            )
        )
    )

    private val smallSegmentDataSet = "Small segment" to Pair(
        BigDecimal(100), listOf(
            Section(
                Section.DisplayMode.PERCENT,
                BigDecimal("15"),
                Color.parseColor("#CE3E61")
            )
        )
    )

    private val largeSegmentDataSet = "Large segment" to Pair(
        BigDecimal(100), listOf(
            Section(
                Section.DisplayMode.PERCENT,
                BigDecimal("75"),
                Color.parseColor("#CE3E61"), Color.parseColor("#FB716F"), Color.parseColor("#FDC0A1")
            )
        )
    )

    private val emptyDataSet = "Empty graph" to Pair(
        BigDecimal(100), listOf(
            Section(
                Section.DisplayMode.PERCENT,
                BigDecimal(0),
                Color.parseColor("#CE3E61")
            )
        )
    )

    val dataSets = listOf(
        fullGraphDataSet,
        smallSegmentDataSet,
        largeSegmentDataSet,
        emptyDataSet
    ).toMap()
}