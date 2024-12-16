package com.android_ai.csc13009.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.android_ai.csc13009.app.data.local.entity.GrammarTopicEntity

@Dao
interface GrammarTopicDao {
    @Insert
    suspend fun insertTopic(topic: GrammarTopicEntity)

    @Query("SELECT * FROM topics WHERE levelId = :levelId")
    suspend fun getTopicsByLevel(levelId: Int): List<GrammarTopicEntity>

    @Query("SELECT * FROM topics WHERE name = :topicName LIMIT 1")
    suspend fun getTopicByName(topicName: String): GrammarTopicEntity?

    @Query("SELECT * FROM topics")
    suspend fun getAllTopics(): List<GrammarTopicEntity>
}