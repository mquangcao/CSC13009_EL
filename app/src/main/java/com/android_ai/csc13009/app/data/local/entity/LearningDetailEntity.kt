package com.android_ai.csc13009.app.data.local.entity
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.time.LocalDate

@Entity(tableName = "LearningDetail")
data class LearningDetailEntity(
    @PrimaryKey
    val id: String,
    val date: String , // Ngày học
    val questionId: String,     // ID của câu hỏi
    var isCorrect: Boolean,  // Kết quả đúng/sai
    val userId: String,       // ID của người dùng
    var type : String,        // Loại câu hỏi
    var isReviewed : Boolean
) : Serializable