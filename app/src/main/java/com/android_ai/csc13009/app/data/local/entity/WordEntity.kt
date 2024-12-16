package com.android_ai.csc13009.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Word")
data class WordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,  // Default value set for auto-generated id
    val word: String,
    val pronunciation: String? = null,  // Set default value for nullable field
    val details: String
)