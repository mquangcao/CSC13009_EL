package com.android_ai.csc13009.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "topics")
data class GrammarTopicEntity(
    @PrimaryKey val id: Int,
    val levelId: Int,
    val name: String
)