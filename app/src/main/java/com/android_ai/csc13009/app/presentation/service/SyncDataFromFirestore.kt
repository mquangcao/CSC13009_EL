package com.android_ai.csc13009.app.presentation.service

import android.content.Context
import android.util.Log
import com.android_ai.csc13009.app.data.local.AppDatabase
import com.android_ai.csc13009.app.data.local.dao.AnswerDao
import com.android_ai.csc13009.app.data.local.entity.UserLessonLearnedEntity
import com.android_ai.csc13009.app.data.remote.repository.FirestoreAnswersRepository
import com.android_ai.csc13009.app.data.remote.repository.FirestoreLessonRepository
import com.android_ai.csc13009.app.data.remote.repository.FirestoreProgressRepository
import com.android_ai.csc13009.app.data.remote.repository.FirestoreQuestionRepository
import com.android_ai.csc13009.app.data.remote.repository.FirestoreTopicRepository
import com.android_ai.csc13009.app.utils.mapper.AnswerMapper
import com.android_ai.csc13009.app.utils.mapper.LessonMapper
import com.android_ai.csc13009.app.utils.mapper.QuestionMapper
import com.android_ai.csc13009.app.utils.mapper.TopicMapper
import com.google.firebase.firestore.FirebaseFirestore

class SyncDataFromFirestore(private val level : String, private val firestore: FirebaseFirestore, context : Context) {
    private var firestoreAnswers : FirestoreAnswersRepository = FirestoreAnswersRepository(firestore)
    private var firestoreQuestions : FirestoreQuestionRepository = FirestoreQuestionRepository(firestore)
    private var firestoreLesson : FirestoreLessonRepository = FirestoreLessonRepository(firestore)
    private var firestoreTopic : FirestoreTopicRepository = FirestoreTopicRepository(firestore)
    private var firestoreProgress : FirestoreProgressRepository = FirestoreProgressRepository(firestore)
    private  val database = AppDatabase.getInstance(context)

    suspend fun run() {
        clearData()
        fetchDataLevel()
        fetchLessonProgress("INQagyhZDxUnZVhYegOVJV7YT2E2")
        //test()
    }

    private suspend fun fetchDataLevel() {
        val topics = firestoreTopic.getTopicList()
        topics.forEach {  topic ->
            val lessons = firestoreLesson.getLessonList(topic.id)
            lessons.forEach { lesson ->
                val questions = firestoreQuestions.getQuestionList(lesson.id, level)
                questions.forEach { question ->
                    // get answers
                    val answers = firestoreAnswers.getAnswerList(question.id)
                    // save to local database
                    answers.forEach { answer ->
                        val entity = AnswerMapper.firestoreToEntity(answer)
                        database.answerDao().insertAnswer(entity)
                    }

                    val entity = QuestionMapper.firestoreToEntity(question)
                    database.questionDao().insertQuestion(entity)
                }

                val lessonEntity = LessonMapper.firestoreToEntity(lesson)
                database.lessonDao().insertLesson(lessonEntity)
            }

            val topicEntity = TopicMapper.firestoreToEntity(topic)
            database.chapterDao().insertChapter(topicEntity)
        }
    }

    private suspend fun fetchLessonProgress(userId : String) {
        val dataFirebase = firestoreProgress.getAllLessonProgress(userId)

        dataFirebase.forEach { lessonProgress ->
            val userProgress = UserLessonLearnedEntity(
                id = lessonProgress.id,
                lessonId = lessonProgress.lessonId,
                userId = lessonProgress.userId,
                totalQuestion = lessonProgress.totalQuestion,
                questionSuccess = lessonProgress.questionSuccess
            )

            database.userProgressDao().insertUserLessonLearned(userProgress)
        }
    }

    private suspend fun clearData() {
        database.answerDao().deleteAll()
        database.questionDao().deleteAllQuestions()
        database.lessonDao().deleteAllLessons()
        database.chapterDao().deleteAllChapters()
    }
}