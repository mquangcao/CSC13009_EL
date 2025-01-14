package com.android_ai.csc13009.app.data.local.repository

import com.android_ai.csc13009.app.data.local.dao.LearningDetailDao
import com.android_ai.csc13009.app.data.local.dao.QuestionDao
import com.android_ai.csc13009.app.data.repository.WordRepository
import com.android_ai.csc13009.app.domain.models.Question
import com.android_ai.csc13009.app.domain.repository.repository.IQuestionRepository
import com.android_ai.csc13009.app.utils.mapper.toDomain

class QuestionRepository(private val questionDao: QuestionDao, private val learningDetailDao: LearningDetailDao, private val wordRepository: WordRepository, private val userId: String) : IQuestionRepository {
    override suspend fun getQuestionsByLessonId(lessonId: Int): List<Question> {
        return questionDao.getQuestionsByLessonId(lessonId).map { question ->
            val answers = questionDao.getAnswersByQuestionId(question.id)
            val answersModels = answers.map { answer ->
                val word = answer.answerWordId?.let { wordRepository.getWordById(it) }
                answer.toDomain(word!!)
            }
            val learningDetails = learningDetailDao.getLearningDetailsByQuestionAndUser(question.id, userId)
            val isCorrect = learningDetails.any { it.isCorrect }
            question.toDomain(answersModels, isCorrect)
        }
    }
}