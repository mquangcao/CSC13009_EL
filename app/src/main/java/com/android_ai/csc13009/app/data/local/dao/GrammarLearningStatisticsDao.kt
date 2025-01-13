package com.android_ai.csc13009.app.data.local.dao

import androidx.room.Dao
import androidx.room.Query

@Dao
interface GrammarLearningStatisticsDao {
    // Đếm số câu trả lời đúng
    @Query("SELECT COUNT(*) FROM GrammarLeanringStatistics WHERE isCorrect = 1")
    suspend fun getCorrectAnswerCount(): Int

    // Đếm tổng số câu đã làm
    @Query("SELECT COUNT(*) FROM GrammarLeanringStatistics")
    suspend fun getTotalAnswerCount(): Int
}