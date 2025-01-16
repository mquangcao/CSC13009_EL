package com.android_ai.csc13009.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android_ai.csc13009.app.data.local.entity.ListeningAnswerEntity
import com.android_ai.csc13009.app.data.local.entity.ListeningQuestionEntity

@Dao
interface ListeningQuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: ListeningQuestionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<ListeningQuestionEntity>)

    @Query("SELECT * FROM ListeningQuestionEntity WHERE id = :questionId")
    suspend fun getQuestionById(questionId: Int): ListeningQuestionEntity?

    @Query("SELECT * FROM ListeningQuestionEntity")
    suspend fun getAllQuestions(): List<ListeningQuestionEntity>

    @Query("DELETE FROM ListeningQuestionEntity WHERE id = :questionId")
    suspend fun deleteQuestion(questionId: Int)

    @Query("DELETE FROM ListeningQuestionEntity")
    suspend fun deleteAllQuestions()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnswer(answer: ListeningAnswerEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnswers(answers: List<ListeningAnswerEntity>)

    @Query("SELECT * FROM ListeningQuestionEntity WHERE lessonId = :lessonId")
    suspend fun getQuestionsByLessonId(lessonId: String): List<ListeningQuestionEntity>

    @Query("SELECT * FROM ListeningAnswer WHERE questionId = :questionId")
    suspend fun getAnswersByQuestionId(questionId: String): List<ListeningAnswerEntity>
}