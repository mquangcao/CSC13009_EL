package com.android_ai.csc13009.app.utils.mapper

import com.android_ai.csc13009.app.data.local.entity.GrammarLearningStatisticsEntity

fun GrammarLearningStatisticsEntity.toDomain(): GrammarLearningStatisticsEntity {
    return GrammarLearningStatisticsEntity(
        id = this.id,
        grammarQuestionId = this.grammarQuestionId,
        userId = this.userId,
        isCorrect = this.isCorrect,
        date = this.date
    )
}