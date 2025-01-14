package com.android_ai.csc13009.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Questions")
data class QuestionsEntity(
    @PrimaryKey
    val id: Int,
    val type: String,
    val lessonId: Int // lesson_id liên kết với LessonEntity
) : Serializable
