package com.android_ai.csc13009.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android_ai.csc13009.app.data.local.entity.WordScheduleEntity

@Dao
interface WordScheduleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(wordSchedule: WordScheduleEntity)

    @Query("SELECT * FROM WordSchedule WHERE userId = :userId AND nextReview <= :currentTime ORDER BY nextReview ASC")
    suspend fun getWordsForReview(userId: String, currentTime: Long): List<WordScheduleEntity>

    @Query("SELECT * FROM WordSchedule WHERE userId = :userId ORDER BY nextReview ASC")
    suspend fun getAllWordSchedules(userId: String): List<WordScheduleEntity>

    @Query("DELETE FROM WordSchedule WHERE userId = :userId AND wordId = :wordId")
    suspend fun deleteWordSchedule(userId: String, wordId: Int)
}
