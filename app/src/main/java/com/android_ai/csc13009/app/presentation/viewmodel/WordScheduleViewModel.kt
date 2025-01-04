package com.android_ai.csc13009.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.android_ai.csc13009.app.data.repository.WordScheduleRepository
import com.android_ai.csc13009.app.domain.models.WordSchedule

class WordScheduleViewModel(
    private val repository: WordScheduleRepository
) : ViewModel() {

    suspend fun insertOrUpdateWordSchedule(userId: String, wordSchedule: WordSchedule) {
        repository.insertOrUpdate(userId, wordSchedule)
    }

    suspend fun getWordsForReview(userId: String, currentTime: Long): List<WordSchedule> {
        return repository.getWordsForReview(userId, currentTime)
    }

    suspend fun getAllWordSchedules(userId: String): List<WordSchedule> {
        return repository.getAllWordSchedules(userId)
    }

    suspend fun deleteWordSchedule(userId: String, wordId: Int) {
        repository.deleteWordSchedule(userId, wordId)
    }
}
