package com.android_ai.csc13009.app.domain.repository

import com.android_ai.csc13009.app.domain.models.WordSchedule

interface IWordScheduleRepository {
    suspend fun insertOrUpdate(userId: String, wordSchedule: WordSchedule)
    suspend fun getWordsForReview(userId: String, currentTime: Long): List<WordSchedule>
    suspend fun getAllWordSchedules(userId: String): List<WordSchedule>
    suspend fun deleteWordSchedule(userId: String, wordId: Int)
}
