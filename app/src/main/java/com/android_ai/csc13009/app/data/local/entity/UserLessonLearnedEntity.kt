package com.android_ai.csc13009.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UserLessonLearned")
data class UserLessonLearnedEntity(
    @PrimaryKey
    val id: String,
    val lessonId: String, // lesson_id liên kết với LessonEntity
    val userId: String,  // user_id liên kết với UserEntity
    val totalQuestion : Int, // tổng số câu hỏi
    val questionSuccess : Int // số câu hỏi trả lời đúng
)
