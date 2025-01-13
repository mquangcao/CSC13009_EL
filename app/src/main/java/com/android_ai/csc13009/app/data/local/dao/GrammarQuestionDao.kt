package com.android_ai.csc13009.app.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.android_ai.csc13009.app.data.local.entity.GrammarQuestionEntity

@Dao
interface GrammarQuestionDao{
    @Query("SELECT * FROM GrammarQuestion WHERE grammarTopicId = :topicId")
    suspend fun getQuestionsByTopicId(topicId: Int): List<GrammarQuestionEntity>

    // getQuestionsById
    @Query("SELECT * FROM GrammarQuestion WHERE grammarQuestionId = :questionId")
    suspend fun getQuestionById(questionId: Int): GrammarQuestionEntity

}