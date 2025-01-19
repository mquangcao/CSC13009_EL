package com.android_ai.csc13009.app.data.local.dao

import androidx.room.*
import com.android_ai.csc13009.app.data.local.entity.StoryEntity

@Dao
interface StoryDao {

    // 1. Thêm một Story
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: StoryEntity)

    // 2. Thêm danh sách Stories
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStories(stories: List<StoryEntity>)

    // 3. Lấy tất cả Stories
    @Query("SELECT * FROM Story ORDER BY storyName ASC")
    suspend fun getAllStories(): List<StoryEntity>

    // 4. Lấy Story theo ID
    @Query("SELECT * FROM Story WHERE id = :id LIMIT 1")
    suspend fun getStoryById(id: String): StoryEntity?

    // 5. Xóa một Story theo ID
    @Query("DELETE FROM Story WHERE id = :id")
    suspend fun deleteStoryById(id: String)

    // 6. Xóa tất cả Stories
    @Query("DELETE FROM Story")
    suspend fun deleteAllStories()

    // 7. Cập nhật một Story
    @Update
    suspend fun updateStory(story: StoryEntity)
}
