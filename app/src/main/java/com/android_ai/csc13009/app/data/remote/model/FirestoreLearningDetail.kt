package com.android_ai.csc13009.app.data.remote.model

data class FirestoreLearningDetail(
    val id: String,
    val date: String, // Ngày học
    val questionId: String,     // ID của câu hỏi
    var correct: Boolean = false,  // Kết quả đúng/sai
    val userId: String,       // ID của người dùng
    var type : String,        // Loại câu hỏi
    var reviewed : Boolean = false
)