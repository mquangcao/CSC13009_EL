package com.android_ai.csc13009.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Lesson")
data class LessonEntity(
    @PrimaryKey
    val id: String,
    val lessonName: String,
    val topicId: String,
    val order : Int,
) : Serializable