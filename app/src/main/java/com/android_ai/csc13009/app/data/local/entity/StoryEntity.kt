package com.android_ai.csc13009.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Story")
data class StoryEntity(
    @PrimaryKey
    var id : String = "",
    var storyName : String = "",
    var thumbnailUrl : String = "",
) {

}