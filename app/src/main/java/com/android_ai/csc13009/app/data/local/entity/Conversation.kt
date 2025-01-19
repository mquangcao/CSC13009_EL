package com.android_ai.csc13009.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Conversation")
data class Conversation(
    @PrimaryKey
    val id : String,
    val storyId : String,
    val gender : String,
    val message : String,
    val type : String,
    val order : Int
) {
}