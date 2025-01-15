package com.android_ai.csc13009.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android_ai.csc13009.app.data.local.entity.ChapterEntity

@Dao
interface ChapterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapter(chapter: ChapterEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapters(chapters: List<ChapterEntity>)

    @Query("SELECT * FROM Chapter WHERE id = :chapterId")
    suspend fun getChapterById(chapterId: String): ChapterEntity?

    @Query("SELECT * FROM Chapter")
    suspend fun getAllChapters(): List<ChapterEntity>

    @Query("DELETE FROM Chapter WHERE id = :chapterId")
    suspend fun deleteChapter(chapterId: String)

    @Query("DELETE FROM Chapter")
    suspend fun deleteAllChapters()
}