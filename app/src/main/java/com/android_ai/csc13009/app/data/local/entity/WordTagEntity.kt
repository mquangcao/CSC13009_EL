package com.android_ai.csc13009.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "WordTag")
data class WordTagEntity(
    @PrimaryKey
    val id: Int,
    val wordId: Int, // word_id liên kết với WordEntity
    val tagId: Int  // tag_id liên kết với UserTagEntity
)
