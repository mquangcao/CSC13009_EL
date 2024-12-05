package com.android_ai.csc13009.app.data.local.dao
import androidx.room.*
import com.android_ai.csc13009.app.data.local.entity.LearningDetailEntity

@Dao
interface LearningDetailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLearningDetail(detail: LearningDetailEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLearningDetails(details: List<LearningDetailEntity>)

    @Query("SELECT * FROM LearningDetail WHERE userId = :userId")
    suspend fun getLearningDetailsByUserId(userId: String): List<LearningDetailEntity>

    @Query("SELECT * FROM LearningDetail WHERE questionId = :questionId AND userId = :userId")
    suspend fun getLearningDetailsByQuestionAndUser(
        questionId: Int,
        userId: String
    ): List<LearningDetailEntity>

    @Query("DELETE FROM LearningDetail WHERE id = :id")
    suspend fun deleteLearningDetailById(id: Int)

    @Query("DELETE FROM LearningDetail")
    suspend fun deleteAllLearningDetails()
}