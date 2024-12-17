package com.android_ai.csc13009.app.presentation.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.android_ai.csc13009.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.ValueFormatter

class MonthlyStatisticsVocabularyFragment : Fragment(R.layout.fragment_monthly_statistics_vocabulary) {

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val barChart: BarChart = view.findViewById(R.id.barChartMonthly)


        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(0f, 5f))
        entries.add(BarEntry(1f, 3f))
        entries.add(BarEntry(2f, 8f))
        entries.add(BarEntry(3f, 6f))
        entries.add(BarEntry(4f, 7f))
        entries.add(BarEntry(5f, 4f))
        entries.add(BarEntry(6f, 9f))
        entries.add(BarEntry(7f, 2f))
        entries.add(BarEntry(8f, 7f))
        entries.add(BarEntry(9f, 6f))
        entries.add(BarEntry(10f, 8f))
        entries.add(BarEntry(11f, 4f))

        // Tạo BarDataSet từ dữ liệu đã nhập
        val barDataSet = BarDataSet(entries, "")
        barDataSet.color = Color.parseColor("#CEB7D4")
        barDataSet.setDrawValues(false) // Ẩn dữ liệu trên cột
        val barData = BarData(barDataSet)

        // Gán dữ liệu cho BarChart
        barChart.data = barData
        barChart.invalidate() // Refresh lại biểu đồ

        // Tuỳ chỉnh khác cho BarChart
        barChart.description.isEnabled = false // Tắt mô tả
        barChart.setFitBars(true) // Điều chỉnh cột để vừa với chiều rộng của biểu đồ

        // Tuỳ chỉnh trục X để hiển thị tên tháng
        val xAxis: XAxis = barChart.xAxis
        xAxis.valueFormatter = MyXAxisFormatter()
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        // Ẩn các viền xung quanh biểu đồ
        barChart.axisLeft.isEnabled = false
        barChart.axisRight.isEnabled = false
        barChart.xAxis.setDrawGridLines(false)
        barChart.setDrawBorders(false)  // Bỏ viền quanh chart
    }

    // Cấu trúc formatter để hiển thị tên tháng
    inner class MyXAxisFormatter : ValueFormatter() {
        private val monthsOfYear = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

        override fun getFormattedValue(value: Float): String {
            return if (value >= 0 && value < monthsOfYear.size) {
                monthsOfYear[value.toInt()]
            } else {
                ""
            }
        }
    }
}