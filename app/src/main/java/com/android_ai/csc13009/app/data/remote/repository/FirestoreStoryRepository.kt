package com.android_ai.csc13009.app.data.remote.repository

import android.util.Log
import com.android_ai.csc13009.app.data.remote.model.Conversation
import com.android_ai.csc13009.app.data.remote.model.FirestoreAnswers
import com.android_ai.csc13009.app.data.remote.model.FirestoreStory
import com.android_ai.csc13009.app.data.remote.model.FirestoreStoryQuestion
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreStoryRepository(private val firestore: FirebaseFirestore)
{
    suspend fun getStoryList() : List<FirestoreStory> {
        return try {
            val data = firestore.collection("story")
                .get()
                .await()
                .documents
                .mapNotNull { document ->
                    val story = FirestoreStory(
                        id = document.id,
                        storyName = document.getString("storyName") ?: "",
                        thumbnailUrl = document.getString("thumbnailUrl") ?: ""
                    )

                    story
                }
            return data
        } catch (e: Exception) {
            Log.e("FirestoreStoryRepository", "Error fetching stories: ${e.message}")
            emptyList()
        }
    }

    suspend fun getConversations(storyId: String) : List<Conversation> {
        return try {
            firestore.collection("storyConversation")
                .whereEqualTo("storyId", storyId)
                .get()
                .await()
                .documents
                .mapNotNull { document ->
                    Conversation(
                        id = document.id,
                        storyId = storyId,
                        gender = document.getString("gender") ?: "",
                        message = document.getString("message") ?: "",
                        type = document.getString("type") ?: "",
                        order = document.getLong("order")?.toInt() ?: 0
                    )
                }
        } catch (e: Exception) {
            Log.e("FirestoreStoryRepository", "Error fetching conversations: ${e.message}")
            emptyList()
        }
    }

    suspend fun getQuestions(storyId: String) : List<FirestoreStoryQuestion> {
        return try {
            firestore.collection("storyQuestion")
                .whereEqualTo("storyId", storyId)
                .get()
                .await()
                .documents
                .mapNotNull { document ->
                    val question = FirestoreStoryQuestion().apply {
                        id = document.id
                        this.storyId = document.getString("storyId") ?: ""
                        question = document.getString("question") ?: ""
                        type = document.getString("type") ?: ""
                    }



                    val answers = firestore.collection("answers")
                        .whereEqualTo("questionId", document.id)
                        .get()
                        .await()
                        .documents
                        .mapNotNull { answers ->
                            FirestoreAnswers(
                                id = answers.id,
                                questionId = answers.getString("questionId") ?: "",
                                text = answers.getString("text") ?: "",
                                isCorrect = answers.getBoolean("isCorrect") ?: false,
                                imgUrl = answers.getString("imgUrl") ?: ""
                            )
                        }

                    question.answers = answers
                    question
                }
        } catch (e: Exception) {
            Log.e("FirestoreStoryRepository", "Error fetching questions: ${e.message}")
            emptyList()
        }
    }


}