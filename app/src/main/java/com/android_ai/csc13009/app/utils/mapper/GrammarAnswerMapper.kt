package com.android_ai.csc13009.app.utils.mapper

import com.android_ai.csc13009.app.data.local.entity.GrammarAnswerEntity
import com.android_ai.csc13009.app.data.remote.model.FirestoreGrammarAnswer
import com.android_ai.csc13009.app.domain.models.GrammarAnswer

fun FirestoreGrammarAnswer.toDomain(): GrammarAnswer {
    return GrammarAnswer(
        id = this.id,
        grammarQuestionId = this.grammarQuestionId,
        answer = this.answer,
        isCorrect = this.isCorrect
    )
}