package com.android_ai.csc13009.app.presentation.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android_ai.csc13009.R
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener

class StatisticsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        // Test Data
        // Vocabulary PieChart
        val pieChartVocabulary: PieChart = findViewById(R.id.pieChartVocabulary)
        val totalVocabulary = 100
        setupPieChart(
            pieChartVocabulary,
            listOf(
                PieEntry(68f, "Correct Vocabulary"), // 68%
                PieEntry(32f, "Incorrect Vocabulary")  // 32%
            ),
            "Total Vocabulary: $totalVocabulary"
        )
        // Add click listener to Vocabulary PieChart
        pieChartVocabulary.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                e?.let {
                    val pieEntry = it as PieEntry
                    when (pieEntry.label) {
                        "Correct Vocabulary" -> navigateToDetailActivity("correctWords")
                        "Incorrect Vocabulary" -> navigateToDetailActivity("incorrectWords")
                    }
                }
            }

            override fun onNothingSelected() {}
        })

        // Lessons PieChart
        val pieChartLessons: PieChart = findViewById(R.id.pieChartLessons)
        val totalLessons = 10
        setupPieChart(
            pieChartLessons,
            listOf(
                PieEntry(6f, "Completed Lessons"),  // 60%
                PieEntry(4f, "Pending Lessons")     // 40%
            ),
            "Total Lessons: $totalLessons"
        )

        // Add click listener to Lessons PieChart
        pieChartLessons.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                e?.let {
                    val pieEntry = it as PieEntry
                    when (pieEntry.label) {
                        "Completed Lessons" -> navigateToDetailActivity("completedLessons")
                        "Pending Lessons" -> navigateToDetailActivity("pendingLessons")
                    }
                }
            }

            override fun onNothingSelected() {}
        })
    }

    // Configure PieChart
    private fun setupPieChart(pieChart: PieChart, entries: List<PieEntry>, centerText: String) {
        val dataSet = PieDataSet(entries, "")
        dataSet.colors = listOf(
            Color.parseColor("#00C1FF"),  // Blue
            Color.parseColor("#FF9500"),  // Orange
            Color.parseColor("#FF00C1")   // Pink
        )
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTextSize = 14f

        val legend = pieChart.legend
        legend.isEnabled = true
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER // Center horizontally
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM     // Align at the bottom
        legend.orientation = Legend.LegendOrientation.HORIZONTAL             // Display horizontally
        legend.setDrawInside(false) // Ensure it is outside the chart


        val data = PieData(dataSet)
        pieChart.data = data
        pieChart.description.isEnabled = false
        pieChart.setDrawEntryLabels(false)
        pieChart.setUsePercentValues(true)
        pieChart.isRotationEnabled = false
        pieChart.centerText = centerText
        pieChart.setCenterTextSize(16f)
        pieChart.invalidate() // Refresh chart
    }

    // Navigate to detail page
    private fun navigateToDetailActivity(type: String) {
        val intent = Intent(this, StatisticsDetailActivity::class.java)
        intent.putExtra("type", type) // Pass data type (correctWords, incorrectWords, ...)
        startActivity(intent)
    }
}
