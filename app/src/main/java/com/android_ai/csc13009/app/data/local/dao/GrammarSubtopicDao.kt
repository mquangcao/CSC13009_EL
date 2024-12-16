package com.android_ai.csc13009.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.android_ai.csc13009.app.data.local.entity.GrammarSubtopicEntity

@Dao
interface GrammarSubtopicDao {
    @Insert
    suspend fun insertSubtopic(subtopic: GrammarSubtopicEntity)

    @Query("SELECT * FROM subtopics WHERE topicId = :topicId")
    suspend fun getSubtopicsByTopicId(topicId: Int): List<GrammarSubtopicEntity>

    @Query("SELECT * FROM subtopics")
    suspend fun getAllSubtopics(): List<GrammarSubtopicEntity> // Thay Any bằng List<GrammarSubtopicEntity>

}