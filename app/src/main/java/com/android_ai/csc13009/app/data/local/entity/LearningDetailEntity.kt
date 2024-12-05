package com.android_ai.csc13009.app.data.local.entity
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "LearningDetail")
data class LearningDetailEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,         // ID tự tăng
    val date: String,        // Ngày học (định dạng ISO 8601, ví dụ: "2024-12-04")
    val questionId: Int,     // ID của câu hỏi
    val isCorrect: Boolean,  // Kết quả đúng/sai
    val userId: String       // ID của người dùng
)