package com.android_ai.csc13009.app.utils.mapper

import com.android_ai.csc13009.app.data.local.entity.AnswersEntity
import com.android_ai.csc13009.app.domain.models.Answer
import com.android_ai.csc13009.app.domain.models.AnswerWord
import com.android_ai.csc13009.app.domain.models.Word
import com.android_ai.csc13009.app.domain.models.WordModel


//fun AnswersEntity.toDomain(answerWord: WordModel) : Answer {
//    return Answer(
//        id = 1,
//        answerWord = TODO(),
//        isCorrect = this.isCorrect,
//        answer = TODO(),
//        thumbNails = TODO()
//    )
//
//}

fun AnswersEntity.toDomain(): AnswerWord {
    val result = AnswerWord()
    result.id = this.id
    result.questionId = this.questionId
    result.text = this.text
//    result.imgUrl = this.imgUrl
    result.isCorrect = this.isCorrect

    return result
}