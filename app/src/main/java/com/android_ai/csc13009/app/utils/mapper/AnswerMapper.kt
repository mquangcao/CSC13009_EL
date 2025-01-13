package com.android_ai.csc13009.app.utils.mapper

import com.android_ai.csc13009.app.data.local.entity.AnswersEntity
import com.android_ai.csc13009.app.domain.models.Answer
import com.android_ai.csc13009.app.domain.repository.model.Word

fun AnswersEntity.toDomain(answerWord: Word) : Answer {
    return Answer(
        answerWord = answerWord,
        isCorrect = this.isCorrect
    )

}