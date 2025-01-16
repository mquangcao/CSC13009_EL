package com.android_ai.csc13009.app.domain.repository
import com.android_ai.csc13009.app.domain.models.GrammarAnswer

interface IGrammarAnswerRepository {
    suspend fun  getAnswersByQuestionId(questionId: String): List<GrammarAnswer>
    suspend fun  getCorrectAnswerByQuestionId(questionId: String): GrammarAnswer?
}