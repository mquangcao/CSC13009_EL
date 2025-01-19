package com.android_ai.csc13009.app.data.remote.model

data class FirestoreListeningLessonProgress (
    val id: String,
    val lessonId: String, // lesson_id liên kết với LessonEntity
    val userId: String,  // user_id liên kết với UserEntity
    val totalQuestion : Int, // tổng số câu hỏi
    val questionSuccess : Int // số câu hỏi trả lời đúng
)