package com.android_ai.csc13009.app.data.local.dao

import androidx.room.*
import com.android_ai.csc13009.app.data.local.entity.StoryQuestion

@Dao
interface StoryQuestionDao {

    // 1. Thêm một câu hỏi
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStoryQuestion(storyQuestion: StoryQuestion)

    // 2. Thêm danh sách câu hỏi
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStoryQuestions(storyQuestions: List<StoryQuestion>)

    // 3. Lấy tất cả câu hỏi
    @Query("SELECT * FROM StoryQuestion ORDER BY id ASC")
    suspend fun getAllStoryQuestions(): List<StoryQuestion>

    // 4. Lấy câu hỏi theo storyId
    @Query("SELECT * FROM StoryQuestion WHERE storyId = :storyId ORDER BY id ASC")
    suspend fun getQuestionsByStoryId(storyId: String): List<StoryQuestion>

    // 5. Lấy câu hỏi theo loại (type)
    @Query("SELECT * FROM StoryQuestion WHERE type = :type ORDER BY id ASC")
    suspend fun getQuestionsByType(type: String): List<StoryQuestion>

    // 6. Xóa câu hỏi theo ID
    @Query("DELETE FROM StoryQuestion WHERE id = :id")
    suspend fun deleteStoryQuestionById(id: String)

    // 7. Xóa tất cả câu hỏi
    @Query("DELETE FROM StoryQuestion")
    suspend fun deleteAllStoryQuestions()

    // 8. Cập nhật một câu hỏi
    @Update
    suspend fun updateStoryQuestion(storyQuestion: StoryQuestion)
}
