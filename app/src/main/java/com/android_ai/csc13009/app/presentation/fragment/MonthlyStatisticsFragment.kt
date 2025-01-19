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

class MonthlyStatisticsFragment : Fragment(R.layout.fragment_monthly_statistics) {

    private lateinit var repository: FirestoreLearningDetailRepository
    private lateinit var userId: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = FirestoreLearningDetailRepository(FirebaseFirestore.getInstance()) // Khởi tạo repository
        val barChart: BarChart = view.findViewById(R.id.barChartMonthly)

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
            val monthlyStats = repository.getMonthlyStatistics(userId)
//            monthlyStats = mapOf(
//                "2025-01" to 8,
//                "2025-02" to 5,
//                "2025-04" to 10,
//                "2025-05" to 10,
//                "2025-06" to 10,
//                "2025-07" to 6,
//            )
            Log.d("MonthlyStatisticsFragment", "Monthly stats: $monthlyStats")
            val formattedStats = mapMonthlyStatsToMonthsOfYear(monthlyStats)
            updateBarChart(barChart, formattedStats)
        }
    }

    private fun mapMonthlyStatsToMonthsOfYear(monthlyStats: Map<String, Int>): Map<String, Int> {
        val monthsOfYear = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        val result = mutableMapOf<String, Int>()

        // Xử lý từng tháng
        for ((date, count) in monthlyStats) {
            val sdf = java.text.SimpleDateFormat("yyyy-MM", java.util.Locale.getDefault())
            val dateObject = sdf.parse(date)
            val calendar = java.util.Calendar.getInstance()
            calendar.time = dateObject

            val monthOfYear = monthsOfYear[calendar.get(java.util.Calendar.MONTH)]
            result[monthOfYear] = (result[monthOfYear] ?: 0) + count
        }
        return result
    }

    private fun updateBarChart(barChart: BarChart, monthlyStats: Map<String, Int>) {
        val monthsOfYear = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

        // Đảm bảo mỗi tháng đều có giá trị, mặc định là 0 nếu không có dữ liệu
        val entries = monthsOfYear.mapIndexed { index, month ->
            val value = monthlyStats[month] ?: 0 // Giá trị mặc định là 0
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
                return monthsOfYear.getOrNull(value.toInt()) ?: ""
            }
        }
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        xAxis.setLabelCount(12, true) // Hiển thị đủ 12 tháng
        xAxis.setLabelRotationAngle(45f) // Xoay nhãn nếu cần thiết
        xAxis.textColor = Color.BLACK
        xAxis.textSize = 10f // Giảm kích thước chữ
    }
}


