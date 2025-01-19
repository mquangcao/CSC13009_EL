package com.android_ai.csc13009.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android_ai.csc13009.app.data.local.entity.ListeningAnswerEntity

@Dao
interface ListeningAnswerDao {
    @Query("Select * from ListeningAnswer where questionId = :questionId")
    suspend fun getAnswersByQuestionId(questionId: String): List<ListeningAnswerEntity>

    @Query("Select * from ListeningAnswer")
    suspend fun getAll(): List<ListeningAnswerEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnswer(answer: ListeningAnswerEntity)

    @Query("Delete from ListeningAnswer")
    suspend fun deleteAll()
}