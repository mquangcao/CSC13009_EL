package com.android_ai.csc13009.app.utils.mapper

import com.android_ai.csc13009.app.data.local.entity.QuestionsEntity
import com.android_ai.csc13009.app.domain.models.AnswerWord
import com.android_ai.csc13009.app.domain.models.Question

//fun QuestionsEntity.toDomain(answers: List<Answer>, isCorrect: Boolean): Question {
//    return Question(
//        id = this.id,
//        type = this.type,
//        question = TODO(),
//        answer = TODO()
//    )
//}

fun QuestionsEntity.toDomain(answers: List<AnswerWord>): Question {
    return Question(
        id = this.id,
        type = this.type,
        answer = answers
    )
}