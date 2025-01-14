package com.android_ai.csc13009.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Lesson")
data class LessonEntity(
    @PrimaryKey
    val id: Int,
    val lessonName: String,
    val description: String,
    val orderNumber: Int,
    val chapterId: Int,
    val status: Boolean
) : Serializable