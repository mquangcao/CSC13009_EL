package com.android_ai.csc13009.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android_ai.csc13009.app.data.local.entity.UserChapterLearnedEntity
import com.android_ai.csc13009.app.data.local.entity.UserLessonLearnedEntity

@Dao
interface UserProgressDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserChapterLearned(chapterLearned: UserChapterLearnedEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserLessonLearned(lessonLearned: UserLessonLearnedEntity)

    @Query("SELECT * FROM UserChapterLearned WHERE id = :userId")
    suspend fun getChaptersLearnedByUser(userId: String): List<UserChapterLearnedEntity>

    @Query("SELECT * FROM UserLessonLearned WHERE userId = :userId")
    suspend fun getLessonsLearnedByUser(userId: String): List<UserLessonLearnedEntity>

    @Query("SELECT * FROM UserLessonLearned WHERE userId = :userId AND lessonId = :lessonId")
    suspend fun getLessonsLearnedByLessonId(userId: String, lessonId : String): List<UserLessonLearnedEntity>

    @Query("DELETE FROM UserChapterLearned WHERE id = :userId")
    suspend fun deleteChaptersLearnedByUser(userId: String)

    @Query("DELETE FROM UserLessonLearned WHERE id = :userId")
    suspend fun deleteLessonsLearnedByUser(userId: String)
}
