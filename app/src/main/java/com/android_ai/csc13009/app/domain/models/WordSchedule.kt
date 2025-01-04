package com.android_ai.csc13009.app.domain.models

data class WordSchedule(
    val wordId: Int,
    val lastReviewed: Long,
    val nextReview: Long,
    val reviewCount: Int,
    val successRate: Float
)
