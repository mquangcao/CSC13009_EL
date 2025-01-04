package com.android_ai.csc13009.app.data.repository

import com.android_ai.csc13009.app.data.local.dao.WordScheduleDao
import com.android_ai.csc13009.app.data.local.entity.WordScheduleEntity
import com.android_ai.csc13009.app.domain.models.WordSchedule
import com.android_ai.csc13009.app.domain.repository.IWordScheduleRepository

class WordScheduleRepository(
    private val dao: WordScheduleDao
) : IWordScheduleRepository {

    override suspend fun insertOrUpdate(userId: String, wordSchedule: WordSchedule) {
        val entity = WordScheduleEntity(
            userId = userId,
            wordId = wordSchedule.wordId,
            lastReviewed = wordSchedule.lastReviewed,
            nextReview = wordSchedule.nextReview,
            reviewCount = wordSchedule.reviewCount,
            successRate = wordSchedule.successRate
        )
        dao.insertOrUpdate(entity)
    }

    override suspend fun getWordsForReview(userId: String, currentTime: Long): List<WordSchedule> {
        return dao.getWordsForReview(userId, currentTime).map {
            WordSchedule(
                wordId = it.wordId,
                lastReviewed = it.lastReviewed,
                nextReview = it.nextReview,
                reviewCount = it.reviewCount,
                successRate = it.successRate
            )
        }
    }

    override suspend fun getAllWordSchedules(userId: String): List<WordSchedule> {
        return dao.getAllWordSchedules(userId).map {
            WordSchedule(
                wordId = it.wordId,
                lastReviewed = it.lastReviewed,
                nextReview = it.nextReview,
                reviewCount = it.reviewCount,
                successRate = it.successRate
            )
        }
    }

    override suspend fun deleteWordSchedule(userId: String, wordId: Int) {
        dao.deleteWordSchedule(userId, wordId)
    }
}
