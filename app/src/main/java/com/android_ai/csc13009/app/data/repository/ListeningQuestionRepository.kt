package com.android_ai.csc13009.app.data.repository

import android.content.Context
import com.android_ai.csc13009.app.data.local.AppDatabase
import com.android_ai.csc13009.app.domain.models.ListeningAnswer
import com.android_ai.csc13009.app.domain.models.ListeningQuestion
import com.android_ai.csc13009.app.domain.repository.IListeningQuestionRepository

class ListeningQuestionRepository(context: Context) : IListeningQuestionRepository {
    val database = AppDatabase.getInstance(context)

    override suspend fun getQuestionsByLessonId(lessonId: String): List<ListeningQuestion> {
        return try {
            val questionEntities = database.listeningQuestionDao().getQuestionsByLessonId(lessonId)
            questionEntities.map {
                ListeningQuestion(
                    id = it.id,
                    question = it.question,
                    type = it.type,
                    answer = getAnswersByQuestionId(it.id) as ArrayList<ListeningAnswer>,
                    audioTranscript = it.audioTranscript
                )
            }

        } catch (e: Exception) {

            emptyList<ListeningQuestion>()
        }

    }

    private suspend fun getAnswersByQuestionId(questionId: String): List<ListeningAnswer> {
        val answerEntities = database.listeningQuestionDao().getAnswersByQuestionId(questionId)
        return answerEntities.map {
            ListeningAnswer(
                id = it.id,
                text = it.text,
                imgUrl = it.imgUrl,
                questionId = questionId,
                isCorrect = it.isCorrect,
            )
        }
    }

}