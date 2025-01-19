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

                    val conversations = firestore.collection("storyConversation")
                        .get()
                        .await()
                        .documents
                        .mapNotNull { conversations ->
                            Conversation(
                                id = conversations.id,
                                gender = conversations.getString("gender") ?: "",
                                message = conversations.getString("message") ?: "",
                                type = conversations.getString("type") ?: ""
                            )
                        }


                    val question = firestore.collection("storyQuestion")
                        .get()
                        .await()
                        .documents
                        .mapNotNull { q ->
                            val storyQuestion = FirestoreStoryQuestion().apply {
                                id = q.id
                                question = q.getString("question") ?: ""
                                type = q.getString("type") ?: ""
                            }

                            val answers = firestore.collection("answers")
                                .whereEqualTo("questionId", q.id)
                                .get()
                                .await()
                                .documents
                                .mapNotNull { answers ->
                                    FirestoreAnswers(
                                        id = answers.id,
                                        text = answers.getString("text") ?: "",
                                        isCorrect = answers.getBoolean("isCorrect") ?: false,
                                        imgUrl = answers.getString("imgUrl") ?: ""
                                    )
                                }

                            storyQuestion.answers = answers
                            storyQuestion
                        }

                    story.conversations = conversations
                    story.questions = question
                    story
                }
            return data
        } catch (e: Exception) {
            Log.e("FirestoreStoryRepository", "Error fetching stories: ${e.message}")
            emptyList()
        }
    }
}