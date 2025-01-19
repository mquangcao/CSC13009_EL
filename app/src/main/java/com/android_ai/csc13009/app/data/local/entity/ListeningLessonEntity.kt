package com.android_ai.csc13009.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ListeningLesson")
data class ListeningLessonEntity(
    @PrimaryKey
    val id: String,
    val lessonName: String,
    val topicId: String,
    val order : Int,
)