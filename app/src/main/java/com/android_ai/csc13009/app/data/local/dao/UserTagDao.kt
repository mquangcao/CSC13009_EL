package com.android_ai.csc13009.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android_ai.csc13009.app.data.local.entity.UserTagEntity

@Dao
interface UserTagDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserTag(userTag: UserTagEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserTags(userTags: List<UserTagEntity>)

    @Query("SELECT * FROM usertag WHERE id = :userId")
    suspend fun getTagsByUserId(userId: String): List<UserTagEntity>

    @Query("DELETE FROM usertag WHERE id = :userId")
    suspend fun deleteTagsByUserId(userId: String)

    @Query("DELETE FROM usertag")
    suspend fun deleteAllUserTags()
}
