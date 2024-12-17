package com.android_ai.csc13009.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "GameData")
data class GameDataEntity (
    @PrimaryKey()
    var gameName: String,
    var highScore: Int,
) : Serializable