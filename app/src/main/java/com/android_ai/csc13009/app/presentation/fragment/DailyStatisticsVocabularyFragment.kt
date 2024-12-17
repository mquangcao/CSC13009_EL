package com.android_ai.csc13009.app.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.android_ai.csc13009.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.ValueFormatter

class DailyStatisticsVocabularyFragment : Fragment(R.layout.fragment_daily_statistics_vocabulary) {

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val barChart: BarChart = view.findViewById(R.id.barChartDaily)

        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(0f, 5f))
        entries.add(BarEntry(1f, 3f))
        entries.add(BarEntry(2f, 8f))
        entries.add(BarEntry(3f, 6f))
        entries.add(BarEntry(4f, 7f))
        entries.add(BarEntry(5f, 4f))
        entries.add(BarEntry(6f, 2f))

        val barDataSet = BarDataSet(entries, "")
        barDataSet.color = 0xFF6200EE.toInt()  // Màu tím cho cột
        barDataSet.setDrawValues(false)  // Ẩn dữ liệu trên cột

        // Giảm độ rộng của cột
        barChart.barData?.barWidth = 0.5f  // Đặt độ rộng của cột nhỏ lại

        val barData = BarData(barDataSet)

        barChart.data = barData
        barChart.invalidate()  // Refresh lại biểu đồ

        barChart.description.isEnabled = false
        barChart.setFitBars(true)

        val xAxis: XAxis = barChart.xAxis
        xAxis.valueFormatter = MyXAxisFormatter()
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        // Ẩn các viền gạch xung quanh biểu đồ
        barChart.axisLeft.isEnabled = false
        barChart.axisRight.isEnabled = false
        barChart.xAxis.setDrawGridLines(false)
        barChart.setDrawBorders(false)  // Bỏ viền quanh chart
    }

    inner class MyXAxisFormatter : ValueFormatter() {
        private val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

        override fun getFormattedValue(value: Float): String {
            return if (value >= 0 && value < daysOfWeek.size) {
                daysOfWeek[value.toInt()]
            } else {
                ""
            }
        }
    }
}