package com.android_ai.csc13009.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UserChapterLearned")
data class UserChapterLearnedEntity(
    @PrimaryKey
    val id: Int,
    val chapterId: Int, // chapter_id liên kết với ChapterEntity
    val userId: String  // user_id liên kết với UserEntity
)
