package com.android_ai.csc13009.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android_ai.csc13009.app.data.local.entity.ListeningLessonEntity

@Dao
interface ListeningLessonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLesson(lesson: ListeningLessonEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLessons(lessons: List<ListeningLessonEntity>)

    @Query("SELECT * FROM ListeningLesson WHERE id = :lessonId")
    suspend fun getLessonById(lessonId: Int): ListeningLessonEntity?

    @Query("SELECT * FROM ListeningLesson WHERE topicId = :topicId ORDER BY `order`")
    suspend fun getLessonsByTopicId(topicId: String): List<ListeningLessonEntity>

    @Query("SELECT * FROM ListeningLesson")
    suspend fun getAllLessons(): List<ListeningLessonEntity>

    @Query("DELETE FROM ListeningLesson WHERE id = :lessonId")
    suspend fun deleteLesson(lessonId: Int)

    @Query("DELETE FROM ListeningLesson")
    suspend fun deleteAllLessons()
}