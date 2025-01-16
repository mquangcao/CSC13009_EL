package com.android_ai.csc13009.app.data.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android_ai.csc13009.app.data.local.AppDatabase
import com.android_ai.csc13009.app.domain.models.Answer
import com.android_ai.csc13009.app.domain.models.AnswerWord
import com.android_ai.csc13009.app.domain.models.Chapter
import com.android_ai.csc13009.app.domain.models.Chapters
import com.android_ai.csc13009.app.domain.models.Lesson
import com.android_ai.csc13009.app.domain.models.Question
import com.android_ai.csc13009.app.domain.repository.IChapterRepository
import com.google.firebase.auth.FirebaseAuth
import kotlin.collections.ArrayList

class ChapterRepository(context: Context) : IChapterRepository {
    private val database = AppDatabase.getInstance(context)

    override suspend fun getChapterList(): List<Chapters> {
        return try {
            val chapters = database.chapterDao().getAllChapters()
            chapters.map {
                val chapter = getChapterDetail(it.id)
                val lessonFilter = filterLesson(chapter.lessons)

                Chapters(
                    id = it.id,
                    chapterName = it.title,
                    totalLesson = lessonFilter.size,
                    lessonFinished = lessonFilter.filter { it.isOpenByProgress }.size,
                    thumbnailUrl = it.thumbnailUrl
                )
            }
        }
        catch (e: Exception) {
            Log.d("ChapterRepository", "getChapterList: ${e.message}")
            emptyList()
        }
    }

    private fun filterLesson(lessons: List<Lesson>): List<Lesson> {
        return lessons.filter { !it.questions.isEmpty() }
    }

    override suspend fun getChapterDetail(chapterId: String): Chapter {
        var isOpenNext = true
        return try {
            val chapter = database.chapterDao().getChapterById(chapterId)
            val lessons = database.lessonDao().getLessonsByChapterId(chapterId)
            val lessonDomain = lessons.map { lesson ->
                val questions = database.questionDao().getQuestionsByLessonId(lesson.id)
                val questionEntity = questions.map { question ->
                    val answers = database.questionDao().getAnswersByQuestionId(question.id)
                    val answerDomain = answers.map { answer ->
                        AnswerWord().apply {
                            id = answer.id
                            text = answer.text
                            imgUrl = answer.imgUrl
                            questionId = answer.questionId
                            isCorrect = answer.isCorrect
                        }
                    }

                    Question(
                        id = question.id,
                        question = question.question,
                        type = question.type,
                        answer = answerDomain as ArrayList<AnswerWord>
                    )
                }

                val userProgress = database.userProgressDao().getLessonsLearnedByLessonId(getUserId(), lesson.id)
                val progressMaxQuestionSuccess = userProgress
                    .maxByOrNull { it.questionSuccess }

                Log.d("QUANG CAO DI CHECK HEHEH", "getChapterDetail: ${userProgress}")

                Lesson(
                    id = lesson.id,
                    lessonName = lesson.lessonName,
                    totalQuestion = questionEntity.size,
                    questionSuccess = progressMaxQuestionSuccess?.questionSuccess ?: 0,
                    order = lesson.order,
                    questions = questionEntity,
                    isOpen = if(userProgress.isNotEmpty()) {
                        true
                    } else if(isOpenNext) {
                            isOpenNext = false
                            true
                    } else {
                        false
                    },
                    isOpenByProgress = userProgress.isNotEmpty(),
                    )
            }
            val filterLesson = filterLesson(lessonDomain)
           Chapter(
                id = chapterId,
                title = chapter?.title ?: "",
                totalLesson = filterLesson.size,
                lessonFinished = filterLesson.filter { it.isOpenByProgress }.size,
                thumbnailUrl = chapter?.thumbnailUrl ?: "",
                lessons = filterLesson.sortedBy { it.order }
           )
        }
        catch (e: Exception) {
            Log.d("ChapterRepository", "getChapterDetail: ${e.message}")
            Chapter(
                id = TODO(),
                title = TODO(),
                totalLesson = TODO(),
                lessonFinished = TODO(),
                thumbnailUrl = TODO(),
                lessons = TODO()
            )
        }
    }

    private fun getUserId(): String {
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser

        return currentUser?.uid ?: ""
    }
}