package com.android_ai.csc13009.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android_ai.csc13009.app.data.local.entity.WordEntity

@Dao
interface WordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: WordEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWords(words: List<WordEntity>)

    @Query("SELECT * FROM Word WHERE id = :wordId")
    suspend fun getWordById(wordId: Int): WordEntity?

    @Query("SELECT * FROM Word WHERE word LIKE :query LIMIT 50")
    suspend fun getWordByName(query: String): List<WordEntity>

    @Query("SELECT * FROM Word WHERE word LIKE :prefix || '%' LIMIT 100")
    suspend fun getSuggestions(prefix: String): List<WordEntity>

    @Query("SELECT * FROM Word")
    suspend fun getAllWords(): List<WordEntity>

    // get the count of all words
    @Query("SELECT COUNT(*) FROM Word")
    suspend fun getWordCount(): Int

    // get a random word
    @Query("SELECT * FROM Word ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomWord(): WordEntity?

    @Query("DELETE FROM Word WHERE id = :wordId")
    suspend fun deleteWord(wordId: Int)

    @Query("DELETE FROM Word")
    suspend fun deleteAllWords()
}


