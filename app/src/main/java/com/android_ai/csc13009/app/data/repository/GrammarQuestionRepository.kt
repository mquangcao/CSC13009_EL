package com.android_ai.csc13009.app.data.repository

import com.android_ai.csc13009.app.data.remote.repository.FirestoreGrammarQuestionRepository
import com.android_ai.csc13009.app.domain.models.GrammarQuestion
import com.android_ai.csc13009.app.domain.repository.IGrammarQuestionRepository
import com.android_ai.csc13009.app.utils.mapper.toDomain


class GrammarQuestionRepository(
    private val firestoreGrammarQuestionRepository: FirestoreGrammarQuestionRepository
) : IGrammarQuestionRepository {

    override suspend fun getQuestionById(questionId: String): GrammarQuestion {
        val firestoreQuestion = firestoreGrammarQuestionRepository.getQuestionById(questionId)
            ?: throw IllegalArgumentException("No question found with ID: $questionId")

        return firestoreQuestion.toDomain()
    }

    override suspend fun getQuestionsByTopicId(topicId: String): List<GrammarQuestion> {
        val firestoreQuestions = firestoreGrammarQuestionRepository.getQuestionsByTopicId(topicId)
        return firestoreQuestions.map { it.toDomain() }
    }
}
