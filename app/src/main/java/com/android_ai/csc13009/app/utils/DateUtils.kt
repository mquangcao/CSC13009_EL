package com.android_ai.csc13009.app.utils

import java.text.SimpleDateFormat
import java.util.*

fun getCurrentDate(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(Date())
}

fun isSameDay(date1: String, date2: String): Boolean {
    return date1 == date2 // So sánh hai ngày dưới dạng chuỗi
}
