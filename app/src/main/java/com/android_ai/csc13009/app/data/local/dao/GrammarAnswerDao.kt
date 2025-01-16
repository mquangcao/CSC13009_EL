package com.android_ai.csc13009.app.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.android_ai.csc13009.app.data.local.entity.GrammarAnswerEntity

@Dao
interface GrammarAnswerDao {
    // Lấy tất cả các câu trả lời thuộc câu hỏi
    @Query("SELECT * FROM GrammarAnswer WHERE grammarQuestionId = :questionId")
    suspend fun getAnswersByQuestionId(questionId: Int): List<GrammarAnswerEntity>

    // Lấy câu trả lời đúng thuộc câu hỏi
    @Query("SELECT * FROM GrammarAnswer WHERE grammarQuestionId = :questionId AND isCorrect = 1")
    suspend fun getCorrectAnswerByQuestionId(questionId: Int): GrammarAnswerEntity?
}