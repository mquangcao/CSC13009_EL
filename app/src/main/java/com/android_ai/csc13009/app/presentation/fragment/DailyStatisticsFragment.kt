package com.android_ai.csc13009.app.presentation.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.remote.repository.FirestoreLearningDetailRepository
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class DailyStatisticsFragment : Fragment(R.layout.fragment_daily_statistics) {

    private lateinit var repository: FirestoreLearningDetailRepository
    private lateinit var userId: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = FirestoreLearningDetailRepository(FirebaseFirestore.getInstance()) // Khởi tạo repository
        val barChart: BarChart = view.findViewById(R.id.barChartDaily)

        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            // Handle user not logged in case
            Toast.makeText(requireContext(), "User not logged in.", Toast.LENGTH_SHORT).show()
            return
        }
        userId = currentUser.uid

        // Load dữ liệu thật
        lifecycleScope.launch {
            val dailyStats = repository.getDailyStatistics(userId)
//             dailyStats = mapOf(
//                "2025-01-17" to 8,
//                "2025-01-18" to 5,
//                "2025-01-19" to 10,
//                "2025-01-20" to 6,
//                "2025-01-21" to 7
//            )

            Log.d("DailyStatisticsFragment", "Daily stats: $dailyStats")
            val formattedStats = mapDailyStatsToDaysOfWeek(dailyStats)
            updateBarChart(barChart, formattedStats)
        }
    }

    private fun mapDailyStatsToDaysOfWeek(dailyStats: Map<String, Int>): Map<String, Int> {
        val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        val result = mutableMapOf<String, Int>()

        // Xử lý từng ngày
        for ((date, count) in dailyStats) {
            val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
            val dateObject = sdf.parse(date)
            val calendar = java.util.Calendar.getInstance()
            calendar.time = dateObject

            val dayOfWeek = daysOfWeek[calendar.get(java.util.Calendar.DAY_OF_WEEK) - 1]
            result[dayOfWeek] = (result[dayOfWeek] ?: 0) + count
        }
        return result
    }

    private fun updateBarChart(barChart: BarChart, dailyStats: Map<String, Int>) {
        val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

        val entries = daysOfWeek.mapIndexed { index, day ->
            val value = dailyStats[day] ?: 0
            BarEntry(index.toFloat(), value.toFloat())
        }

        val barDataSet = BarDataSet(entries, "")
        barDataSet.color = Color.parseColor("#D3ACF8")
        barDataSet.setDrawValues(false)

        val barData = BarData(barDataSet)
        barData.barWidth = 0.6f

        barChart.data = barData
        barChart.invalidate()

        // Tùy chỉnh biểu đồ
        barChart.description.isEnabled = false
        barChart.setFitBars(true)
        barChart.axisLeft.isEnabled = false
        barChart.axisRight.isEnabled = false
        barChart.setDrawBorders(false)
        barChart.legend.isEnabled = false

        // Tùy chỉnh trục X
        val xAxis: XAxis = barChart.xAxis
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return daysOfWeek.getOrNull(value.toInt()) ?: ""
            }
        }
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        xAxis.textColor = Color.BLACK
        xAxis.textSize = 12f
    }
}


