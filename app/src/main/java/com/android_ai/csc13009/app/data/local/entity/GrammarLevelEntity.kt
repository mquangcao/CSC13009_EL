package com.android_ai.csc13009.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "levels")
data class GrammarLevelEntity(
    @PrimaryKey val id: Int,
    val name: String
)