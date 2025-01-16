package com.android_ai.csc13009.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Chapter")
data class ChapterEntity(
    @PrimaryKey
    val id : String,
    val title : String,
    val thumbnailUrl : String

)
