package com.android_ai.csc13009.app.data.repository

import android.content.Context
import com.android_ai.csc13009.app.data.local.AppDatabase
import com.android_ai.csc13009.app.domain.models.AnswerWord
import com.android_ai.csc13009.app.domain.models.Question

class QuestionRepository(private val context : Context) {
    private val database = AppDatabase.getInstance(context)

    suspend fun getQuestion(questionId : String) : Question{
        val data = database.questionDao().getQuestionById(questionId)
        if (data != null) {
            val answerWord = database.questionDao().getAnswersByQuestionId(questionId)
            val answers = answerWord.map {
                AnswerWord().apply {
                    id = it.id
                    text = it.text
                    imgUrl = it.imgUrl
                    this.questionId = it.questionId
                    isCorrect = it.isCorrect
                }
            }


            return Question(
                id = data.id,
                question = data.question,
                type = data.type,
                answer = ArrayList(answers)
            )
        }
        return Question("", "", "", ArrayList())
    }

}

