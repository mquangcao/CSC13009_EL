package com.android_ai.csc13009.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android_ai.csc13009.app.data.local.entity.AnswersEntity
import com.android_ai.csc13009.app.data.local.entity.QuestionsEntity

@Dao
interface QuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: QuestionsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<QuestionsEntity>)

    @Query("SELECT * FROM Questions WHERE id = :questionId")
    suspend fun getQuestionById(questionId: String): QuestionsEntity?

    @Query("SELECT * FROM Questions")
    suspend fun getAllQuestions(): List<QuestionsEntity>

    @Query("DELETE FROM Questions WHERE id = :questionId")
    suspend fun deleteQuestion(questionId: Int)

    @Query("DELETE FROM Questions")
    suspend fun deleteAllQuestions()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnswer(answer: AnswersEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnswers(answers: List<AnswersEntity>)

    @Query("SELECT * FROM Questions WHERE lessonId = :lessonId")
    suspend fun getQuestionsByLessonId(lessonId: String): List<QuestionsEntity>

    @Query("SELECT * FROM Answers WHERE questionId = :questionId")
    suspend fun getAnswersByQuestionId(questionId: String): List<AnswersEntity>
}
