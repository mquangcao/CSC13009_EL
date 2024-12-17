package com.android_ai.csc13009.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subtopics")
data class GrammarSubtopicEntity(
    @PrimaryKey val id: Int,
    val topicId: Int,
    val name: String,
    val content: String,
    val structures: String,
    val examples: String
)