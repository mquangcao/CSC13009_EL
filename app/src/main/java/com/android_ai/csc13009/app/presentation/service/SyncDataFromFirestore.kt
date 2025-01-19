package com.android_ai.csc13009.app.presentation.service

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android_ai.csc13009.app.data.local.AppDatabase
import com.android_ai.csc13009.app.data.local.dao.AnswerDao
import com.android_ai.csc13009.app.data.local.entity.AnswersEntity
import com.android_ai.csc13009.app.data.local.entity.Conversation
import com.android_ai.csc13009.app.data.local.entity.QuestionsEntity
import com.android_ai.csc13009.app.data.local.entity.StoryEntity
import com.android_ai.csc13009.app.data.local.entity.StoryQuestion
import com.android_ai.csc13009.app.data.local.entity.UserLessonLearnedEntity
import com.android_ai.csc13009.app.data.remote.repository.FirestoreAnswersRepository
import com.android_ai.csc13009.app.data.remote.repository.FirestoreLessonRepository
import com.android_ai.csc13009.app.data.remote.repository.FirestoreProgressRepository
import com.android_ai.csc13009.app.data.remote.repository.FirestoreQuestionRepository
import com.android_ai.csc13009.app.data.remote.repository.FirestoreStoryRepository
import com.android_ai.csc13009.app.data.remote.repository.FirestoreTopicRepository
import com.android_ai.csc13009.app.utils.mapper.AnswerMapper
import com.android_ai.csc13009.app.utils.mapper.LessonMapper
import com.android_ai.csc13009.app.utils.mapper.QuestionMapper
import com.android_ai.csc13009.app.utils.mapper.TopicMapper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SyncDataFromFirestore(private val level : String, private val firestore: FirebaseFirestore, context : Context) {
    private var firestoreAnswers : FirestoreAnswersRepository = FirestoreAnswersRepository(firestore)
    private var firestoreQuestions : FirestoreQuestionRepository = FirestoreQuestionRepository(firestore)
    private var firestoreLesson : FirestoreLessonRepository = FirestoreLessonRepository(firestore)
    private var firestoreTopic : FirestoreTopicRepository = FirestoreTopicRepository(firestore)
    private var firestoreProgress : FirestoreProgressRepository = FirestoreProgressRepository(firestore)
    private var firestoreStory : FirestoreStoryRepository = FirestoreStoryRepository(firestore)
    private  val database = AppDatabase.getInstance(context)

    suspend fun run() {
        clearData()
        syncStories()
        fetchDataLevel()
        fetchLessonProgress(getUserId())

    }

    private suspend fun test() {
        val questions = database.storyDao().getAllStories()
        questions.forEach { question ->
            Log.d("SyncDataFromFirestore", "Story: ${question}")
        }

        val conversations = database.conversationDao().getAllConversations()
        conversations.forEach { conversation ->
            Log.d("SyncDataFromFirestore", "Conversation: ${conversation}")
        }

        val storyQuestions = database.storyQuestionDao().getAllStoryQuestions()
        storyQuestions.forEach { question ->
            Log.d("SyncDataFromFirestore", "Question: ${question}")

            val answers = database.answerDao().getAnswersByQuestionId(question.id)
            Log.d("SyncDataFromFirestore", "Answers: ${answers.size}")
            answers.forEach { answer ->
                Log.d("SyncDataFromFirestore", "Answer: ${answer}")
            }
        }


    }

    private fun getUserId() : String {
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser

        return currentUser?.uid ?: ""
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

    private suspend fun syncStories() {
        val stories = firestoreStory.getStoryList()

        stories.forEach { story ->
            val storyEntity = StoryEntity(
                id = story.id,
                storyName = story.storyName,
                thumbnailUrl = story.thumbnailUrl
            )

            database.storyDao().insertStory(storyEntity)

            val conversations = firestoreStory.getConversations(story.id)
            conversations.forEach { conversation ->
                val conversationEntity = Conversation(
                    id = conversation.id,
                    storyId = conversation.storyId,
                    gender = conversation.gender,
                    message = conversation.message,
                    type = conversation.type,
                    order = conversation.order,
                )

                database.conversationDao().insertConversation(conversationEntity)
            }

            val question = firestoreStory.getQuestions(story.id)
            question.forEach { q ->
                //val answers = firestoreStory.(q.id)

                val questionEntity = StoryQuestion(
                    id = q.id,
                    storyId = q.storyId,
                    question = q.question,
                    type = q.type
                )
                database.storyQuestionDao().insertStoryQuestion(questionEntity)

                q.answers.forEach{ answer ->

                    val data = AnswersEntity(
                        id = answer.id,
                        questionId = answer.questionId,
                        text = answer.text,
                        isCorrect = answer.isCorrect,
                        imgUrl = answer.imgUrl
                    )
                    database.answerDao().insertAnswer(data)
                }
            }
        }
        test()
    }

    private suspend fun clearData() {
        database.answerDao().deleteAll()
        database.questionDao().deleteAllQuestions()
        database.lessonDao().deleteAllLessons()
        database.chapterDao().deleteAllChapters()
        database.userProgressDao().deleteLessonsLearned()
    }
}