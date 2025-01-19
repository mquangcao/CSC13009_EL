package com.android_ai.csc13009.app.presentation.service

class CalculateNextReviewDateUseCase {
    operator fun invoke(lastReviewed: Long, reviewCount: Int, successRate: Float): Long {
        val baseInterval = when (reviewCount) {
            0 -> 1 // First review after 1 day
            1 -> 3 // Second review after 3 days
            else -> (reviewCount * 2 * successRate).toLong() // Dynamic interval
        }
        val intervalInMillis = baseInterval * 24 * 60 * 60 * 1000L // Convert days to milliseconds
        return lastReviewed + intervalInMillis
    }
}

