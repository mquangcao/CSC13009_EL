package com.android_ai.csc13009.app.domain.models

data class Lesson(
    val imageRes: Int,      // Resource ID cho ảnh
    val title: String,      // Tiêu đề bài học
    val questionCount: Int, // Số câu hỏi
    val completionRate: Int, // Tỉ lệ hoàn thành (0-100)
    val isUnlocked: Boolean = false // Bài học đã mở hay chưa
)