package com.android_ai.csc13009.app.data.repository

import android.content.Context
import com.android_ai.csc13009.app.data.local.AppDatabase
import com.android_ai.csc13009.app.domain.models.Answer
import com.android_ai.csc13009.app.domain.models.AnswerWord
import com.android_ai.csc13009.app.domain.models.Question
import com.android_ai.csc13009.app.domain.models.Story

class StoryRepository(context: Context) {
    private val database = AppDatabase.getInstance(context)

    suspend fun getAllStory() : List<Story> {
        val data = database.storyDao().getAllStories()
        return data.map {
            Story(
                id = it.id,
                title = it.storyName,
                imgUrl = it.thumbnailUrl
            )
        }
    }

    suspend fun getConversationsByStoryId(storyId: String) = database.conversationDao().getConversationsByStoryId(storyId)

    suspend fun getStoryQuestion(storyId: String) : List<Question> {
        val questions = database.storyQuestionDao().getQuestionsByStoryId(storyId)
        val data =  questions.map { question ->
            val answers = database.answerDao().getAnswersByQuestionId(question.id)
            val answerDomain = answers.map { answer ->
                AnswerWord().apply {
                    id = answer.id
                    text = answer.text
                    imgUrl = answer.imgUrl
                    this.questionId = question.id
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

        return data
    }
}