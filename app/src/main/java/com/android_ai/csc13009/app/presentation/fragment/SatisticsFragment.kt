package com.android_ai.csc13009.app.presentation.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.presentation.activity.StatisticsDetailActivity
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener

class StatisticsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_statistics, container, false)

        // Test Data
        // Vocabulary PieChart
        val pieChartVocabulary: PieChart = view.findViewById(R.id.pieChartVocabulary)
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
        val pieChartLessons: PieChart = view.findViewById(R.id.pieChartLessons)
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

        return view
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
        legend.orientation = Legend.LegendOrientation.HORIZONTAL             // Display horizontally
        legend.setDrawInside(false) // Ensure it is outside the chart


        val data = PieData(dataSet)
        pieChart.setUsePercentValues(true)
        pieChart.data = data
        pieChart.description.isEnabled = false
        pieChart.setDrawEntryLabels(false)
        pieChart.setUsePercentValues(true)
        pieChart.isRotationEnabled = false
        pieChart.centerText = centerText
        pieChart.setCenterTextSize(14f)
        pieChart.invalidate() // Refresh chart
    }

    // Navigate to StatisticsDetailActivity
    private fun navigateToDetailActivity(type: String) {
        val intent = Intent(activity, StatisticsDetailActivity::class.java)
        intent.putExtra("type", type)  // Pass data type (correctWords, incorrectWords, etc.)
        startActivity(intent)
    }
}
