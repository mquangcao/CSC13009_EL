package com.android_ai.csc13009.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android_ai.csc13009.app.data.local.entity.LessonEntity

@Dao
interface LessonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLesson(lesson: LessonEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLessons(lessons: List<LessonEntity>)

    @Query("SELECT * FROM Lesson WHERE id = :lessonId")
    suspend fun getLessonById(lessonId: Int): LessonEntity?

    @Query("SELECT * FROM Lesson WHERE topicId = :chapterId")
    suspend fun getLessonsByChapterId(chapterId: String): List<LessonEntity>

    @Query("SELECT * FROM Lesson")
    suspend fun getAllLessons(): List<LessonEntity>

    @Query("DELETE FROM Lesson WHERE id = :lessonId")
    suspend fun deleteLesson(lessonId: Int)

    @Query("DELETE FROM Lesson")
    suspend fun deleteAllLessons()
}
