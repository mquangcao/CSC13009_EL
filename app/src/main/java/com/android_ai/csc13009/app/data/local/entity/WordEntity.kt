package com.android_ai.csc13009.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Word")
data class WordEntity(
    @PrimaryKey
    val id: Int,
    val word: String,
    val meaning: String,
    val audioUrl: String?,
    val synonyms: String?,
    val exampleSentence: String?,
    val image: String?
)
