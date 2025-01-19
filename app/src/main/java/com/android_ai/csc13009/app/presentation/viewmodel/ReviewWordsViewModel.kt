package com.android_ai.csc13009.app.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android_ai.csc13009.app.data.repository.WordScheduleRepository
import com.android_ai.csc13009.app.domain.models.WordSchedule
import com.android_ai.csc13009.app.domain.repository.IWordScheduleRepository
import com.android_ai.csc13009.app.presentation.service.CalculateNextReviewDateUseCase
import kotlinx.coroutines.launch

class ReviewWordsViewModel(
    private val repository: WordScheduleRepository,
    private val calculateNextReviewDateUseCase: CalculateNextReviewDateUseCase,
    private val userId: String
) : ViewModel() {

    private val _wordsToReview = MutableLiveData<List<WordSchedule>>()
    val wordsToReview: LiveData<List<WordSchedule>> = _wordsToReview

    fun loadWordsForReview() {
        viewModelScope.launch {
            val currentTime = System.currentTimeMillis()
            _wordsToReview.value = repository.getWordsForReview(userId, currentTime)
        }
    }

    fun updateWordReview(word: WordSchedule, isCorrect: Boolean) {
        viewModelScope.launch {
            val newReviewCount = word.reviewCount + 1
            val newSuccessRate = if (isCorrect) {
                (word.successRate * word.reviewCount + 1) / newReviewCount
            } else {
                word.successRate * word.reviewCount / newReviewCount
            }
            val newNextReview = calculateNextReviewDateUseCase(
                word.lastReviewed,
                newReviewCount,
                newSuccessRate
            )
            val updatedWord = word.copy(
                lastReviewed = System.currentTimeMillis(),
                nextReview = newNextReview,
                reviewCount = newReviewCount,
                successRate = newSuccessRate
            )
            repository.insertOrUpdate(userId, updatedWord)
            loadWordsForReview() // Reload the list
        }
    }
}
