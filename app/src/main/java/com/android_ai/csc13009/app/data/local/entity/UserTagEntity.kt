package com.android_ai.csc13009.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// các tag cho user định nghĩa
@Entity(tableName = "UserTag")
data class UserTagEntity(
    @PrimaryKey
    val id: Int,
    val tagName: String,
    val userId: String, // user_id liên kết với UserEntity
    val colorTag: String
)
