package com.android_ai.csc13009.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "GrammarQuestion")
data class GrammarQuestionEntity(
    @PrimaryKey val grammarQuestionId: Int,
    val grammarTopicId: Int,
    val name: String,
    val type: String
)