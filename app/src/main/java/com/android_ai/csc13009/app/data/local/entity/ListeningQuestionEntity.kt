package com.android_ai.csc13009.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ListeningQuestionEntity")
class ListeningQuestionEntity (
    @PrimaryKey
    val id: String = "",
    val level: String = "",
    val lessonId: String = "",
    val question: String = "",
    val type: String = "",
    val audioTranscript: String = "",
)