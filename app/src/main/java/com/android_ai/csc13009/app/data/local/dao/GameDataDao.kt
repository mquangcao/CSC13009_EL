package com.android_ai.csc13009.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android_ai.csc13009.app.data.local.entity.GameDataEntity

@Dao
interface GameDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGameData(gameData: GameDataEntity)

    @Query("SELECT * FROM GameData WHERE gameName = :gameName")
    suspend fun getGameDataByName(gameName: String): GameDataEntity?

    @Query("UPDATE GameData SET highScore = :newHighScore WHERE gameName = :gameName")
    suspend fun updateHighScore(gameName: String, newHighScore: Int)

}