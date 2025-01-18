package com.android_ai.csc13009.app.presentation.service

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android_ai.csc13009.app.data.local.AppDatabase
import com.android_ai.csc13009.app.data.local.dao.AnswerDao
import com.android_ai.csc13009.app.data.local.entity.UserLessonLearnedEntity
import com.android_ai.csc13009.app.data.remote.repository.FirestoreAnswersRepository
import com.android_ai.csc13009.app.data.remote.repository.FirestoreLessonRepository
import com.android_ai.csc13009.app.data.remote.repository.FirestoreListeningAnswerRepository
import com.android_ai.csc13009.app.data.remote.repository.FirestoreListeningLessonRepository
import com.android_ai.csc13009.app.data.remote.repository.FirestoreListeningQuestionRepository
import com.android_ai.csc13009.app.data.remote.repository.FirestoreListeningTopicRepository
import com.android_ai.csc13009.app.data.remote.repository.FirestoreProgressRepository
import com.android_ai.csc13009.app.data.remote.repository.FirestoreQuestionRepository
import com.android_ai.csc13009.app.data.remote.repository.FirestoreTopicRepository
import com.android_ai.csc13009.app.utils.mapper.AnswerMapper
import com.android_ai.csc13009.app.utils.mapper.LessonMapper
import com.android_ai.csc13009.app.utils.mapper.QuestionMapper
import com.android_ai.csc13009.app.utils.mapper.TopicMapper
import com.android_ai.csc13009.app.utils.mapper.toEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SyncDataFromFirestore(private val level : String, private val firestore: FirebaseFirestore, context : Context) {
    private var firestoreAnswers : FirestoreAnswersRepository = FirestoreAnswersRepository(firestore)
    private var firestoreQuestions : FirestoreQuestionRepository = FirestoreQuestionRepository(firestore)
    private var firestoreLesson : FirestoreLessonRepository = FirestoreLessonRepository(firestore)
    private var firestoreTopic : FirestoreTopicRepository = FirestoreTopicRepository(firestore)
    private var firestoreProgress : FirestoreProgressRepository = FirestoreProgressRepository(firestore)

//  listening
    private var firestoreListeningTopic : FirestoreListeningTopicRepository = FirestoreListeningTopicRepository(firestore)
    private var firestoreListeningLesson : FirestoreListeningLessonRepository = FirestoreListeningLessonRepository(firestore)
    private var firestoreListeningQuestion : FirestoreListeningQuestionRepository = FirestoreListeningQuestionRepository(firestore)
    private var firestoreListeningAnswer : FirestoreListeningAnswerRepository = FirestoreListeningAnswerRepository(firestore)

    private  val database = AppDatabase.getInstance(context)

    suspend fun run() {
        clearData()
        fetchDataLevel()
        fetchLessonProgress(getUserId())
        //test()
    }

    private fun getUserId() : String {
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser

        return currentUser?.uid ?: ""
    }

    private suspend fun fetchDataLevel() {
        // word topics
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

        //listening topic
        fetchListeningTopics()
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


        database.listeningTopicDao().deleteAllTopic()
        database.listeningAnswerDao().deleteAll()
        database.listeningLessonDao().deleteAllLessons()
        database.listeningQuestionDao().deleteAllQuestions()
    }


    private suspend fun fetchListeningTopics() {
        val topics = firestoreListeningTopic.getTopicList()
        topics.forEach() {
            topic ->
            val lessons = firestoreListeningLesson.getLessonList(topic.id)

            lessons.forEach {
                lesson ->
                val questions = firestoreListeningQuestion.getQuestionList(lesson.id, level)
                questions.forEach {
                    question ->
                    val answers = firestoreListeningAnswer.getAnswerList(question.id)
                    answers.forEach {
                        answer ->
                        val entity = answer.toEntity()
                        database.listeningAnswerDao().insertAnswer(entity)
                    }
                    val entity = question.toEntity()
                    database.listeningQuestionDao().insertQuestion(entity)
                }
                val lessonEntity = lesson.toEntity()
                database.listeningLessonDao().insertLesson(lessonEntity)
            }
            val topicEntity = topic.toEntity()
            database.listeningTopicDao().insert(topicEntity)
        }
    }
}