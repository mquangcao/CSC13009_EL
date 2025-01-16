package com.android_ai.csc13009.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.android_ai.csc13009.app.data.local.entity.AnswersEntity

@Dao
interface AnswerDao {
//    suspend fun insertAnswer(answer: AnswerEntity)
    @Query("Select * from Answers where questionId = :questionId")
    suspend fun getAnswersByQuestionId(questionId: Int): List<AnswersEntity>

    @Query("Select * from Answers")
    suspend fun getAll(): List<AnswersEntity>

    @Insert
    suspend fun insertAnswer(answer: AnswersEntity)

    @Query("Delete from Answers")
    suspend fun deleteAll()
}