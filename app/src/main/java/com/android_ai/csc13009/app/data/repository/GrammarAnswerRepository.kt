package com.android_ai.csc13009.app.data.repository

import com.android_ai.csc13009.app.data.remote.repository.FirestoreGrammarAnswerRepository
import com.android_ai.csc13009.app.domain.models.GrammarAnswer
import com.android_ai.csc13009.app.domain.repository.IGrammarAnswerRepository
import com.android_ai.csc13009.app.utils.mapper.toDomain

class GrammarAnswerRepository(private val firestoreGrammarAnswerRepository: FirestoreGrammarAnswerRepository)
    : IGrammarAnswerRepository {

    override suspend fun getAnswersByQuestionId(questionId: String): List<GrammarAnswer> {
        val firestoreAnswers = firestoreGrammarAnswerRepository.getAnswersByQuestionId(questionId)
        return firestoreAnswers.map { it.toDomain() } // Calling toDomain() on FirestoreGrammarAnswer
    }

    override suspend fun getCorrectAnswerByQuestionId(questionId: String): GrammarAnswer? {
        val firestoreCorrectAnswer = firestoreGrammarAnswerRepository.getCorrectAnswerByQuestionId(questionId)
        return firestoreCorrectAnswer?.toDomain() // Calling toDomain() to map to GrammarAnswer
    }
}

