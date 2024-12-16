package com.android_ai.csc13009.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.android_ai.csc13009.app.data.local.entity.GrammarLevelEntity

@Dao
interface GrammarLevelDao {
    @Insert
    suspend fun insertLevel(level: GrammarLevelEntity)

    @Query("SELECT * FROM levels")
    suspend fun getAllLevels(): List<GrammarLevelEntity>
}