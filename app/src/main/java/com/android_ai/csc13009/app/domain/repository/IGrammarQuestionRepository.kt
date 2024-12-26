package com.android_ai.csc13009.app.domain.repository

import com.android_ai.csc13009.app.domain.models.GrammarQuestion

interface IGrammarQuestionRepository {
    suspend fun getQuestionsByTopicId(topicId: Int): List<GrammarQuestion>
    suspend fun  getQuestionById(questionId: Int): GrammarQuestion
}