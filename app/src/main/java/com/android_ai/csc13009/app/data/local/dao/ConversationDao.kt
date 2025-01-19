package com.android_ai.csc13009.app.data.local.dao

import androidx.room.*
import com.android_ai.csc13009.app.data.local.entity.Conversation

@Dao
interface ConversationDao {

    // 1. Thêm một Conversation
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversation(conversation: Conversation)

    // 2. Thêm danh sách Conversations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversations(conversations: List<Conversation>)

    // 3. Lấy tất cả Conversations
    @Query("SELECT * FROM Conversation ORDER BY `order` ASC")
    suspend fun getAllConversations(): List<Conversation>

    // 4. Lấy Conversations theo storyId
    @Query("SELECT * FROM Conversation WHERE storyId = :storyId ORDER BY `order` ASC")
    suspend fun getConversationsByStoryId(storyId: String): List<Conversation>

    // 5. Lấy Conversations theo giới tính
    @Query("SELECT * FROM Conversation WHERE gender = :gender ORDER BY `order` ASC")
    suspend fun getConversationsByGender(gender: String): List<Conversation>

    // 6. Xóa một Conversation theo ID
    @Query("DELETE FROM Conversation WHERE id = :id")
    suspend fun deleteConversationById(id: String)

    // 7. Xóa tất cả Conversations
    @Query("DELETE FROM Conversation")
    suspend fun deleteAllConversations()

    // 8. Cập nhật một Conversation
    @Update
    suspend fun updateConversation(conversation: Conversation)
}
