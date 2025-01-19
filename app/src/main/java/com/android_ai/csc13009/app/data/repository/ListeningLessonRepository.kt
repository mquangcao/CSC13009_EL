package com.android_ai.csc13009.app.data.repository

import android.content.Context
import com.android_ai.csc13009.app.data.local.AppDatabase
import com.android_ai.csc13009.app.domain.models.ListeningLesson
import com.android_ai.csc13009.app.domain.repository.IListeningLessonRepository
import com.google.firebase.auth.FirebaseAuth

class ListeningLessonRepository(val context: Context) : IListeningLessonRepository {
    val database = AppDatabase.getInstance(context)
    override suspend fun getLessonsByTopicId(topicId: String): List<ListeningLesson> {
        var isOpenNext = true
        return try {
            val lessonEntities = database.listeningLessonDao().getLessonsByTopicId(topicId)

            val questionRepository = ListeningQuestionRepository(context)
            lessonEntities.map {
                val questions = questionRepository.getQuestionsByLessonId(it.id)


                val userProgress = database.userProgressDao().getLessonsLearnedByLessonId(getUserId(), it.id)
                val progressMaxQuestionSuccess = userProgress
                    .maxByOrNull { it.questionSuccess }

                ListeningLesson(
                    id = it.id,
                    questions = questions,
                    lessonName = it.lessonName,
                    totalQuestion = questions.size,
                    questionSuccess = progressMaxQuestionSuccess?.questionSuccess ?: 0,
                    order = it.order,
                    isOpen = if(userProgress.isNotEmpty()) {
                        true
                    } else if(isOpenNext) {
                        isOpenNext = false
                        true
                    } else {
                        false
                    },
                    isOpenByProgress = userProgress.isNotEmpty()
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun getUserId(): String {
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser

        return currentUser?.uid ?: ""
    }

//    private fun getLessonDetails
}