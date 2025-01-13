package com.android_ai.csc13009.app.utils.mapper

import com.android_ai.csc13009.app.data.local.entity.GrammarAnswerEntity
import com.android_ai.csc13009.app.domain.models.GrammarAnswer

fun GrammarAnswerEntity.toDomain(): GrammarAnswer{
    return GrammarAnswer(
        grammarAnswerId = this.grammarAnswerId,
        grammarQuestionId = this.grammarQuestionId,
        answer = this.answer,
        isCorrect = this.isCorrect
    )
}