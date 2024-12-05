package com.android_ai.csc13009.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android_ai.csc13009.app.data.local.entity.WordTagEntity

@Dao
interface WordTagDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWordTag(wordTag: WordTagEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWordTags(wordTags: List<WordTagEntity>)

    @Query("SELECT * FROM wordtag WHERE id = :wordId")
    suspend fun getTagsByWordId(wordId: Int): List<WordTagEntity>

    @Query("DELETE FROM wordtag WHERE id = :wordId")
    suspend fun deleteTagsByWordId(wordId: Int)

    @Query("DELETE FROM wordtag")
    suspend fun deleteAllWordTags()
}