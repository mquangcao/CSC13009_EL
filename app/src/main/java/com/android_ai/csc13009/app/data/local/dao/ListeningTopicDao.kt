package com.android_ai.csc13009.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android_ai.csc13009.app.data.local.entity.ListeningTopicEntity

@Dao
interface ListeningTopicDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(topic: ListeningTopicEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(topics: List<ListeningTopicEntity>)

    @Query("SELECT * FROM ListeningTopic WHERE id = :topicId")
    suspend fun getTopicById(topicId: String): ListeningTopicEntity?

    @Query("SELECT * FROM ListeningTopic")
    suspend fun getAllTopic(): List<ListeningTopicEntity>

    @Query("DELETE FROM ListeningTopic WHERE id = :topicId")
    suspend fun deleteTopic(topicId: String)

    @Query("DELETE FROM ListeningTopic")
    suspend fun deleteAllTopic()
}