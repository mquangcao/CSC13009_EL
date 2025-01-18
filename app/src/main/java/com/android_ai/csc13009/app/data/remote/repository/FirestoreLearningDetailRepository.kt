package com.android_ai.csc13009.app.data.remote.repository

import android.util.Log
import com.android_ai.csc13009.app.data.remote.model.FirestoreGrammarAnswer
import com.android_ai.csc13009.app.data.remote.model.FirestoreGrammarQuestion
import com.android_ai.csc13009.app.data.remote.model.FirestoreLearningDetail
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreLearningDetailRepository(private val firestore: FirebaseFirestore) {
    suspend fun createLearningDetail(userId: String, questionId: String, isCorrect : Boolean, type : String): String? {
        return try {
            val id = firestore.collection("learningDetail").document().id
            val learningDetail = FirestoreLearningDetail(
                id = id,
                date = System.currentTimeMillis().toString(),
                questionId = questionId,
                correct = isCorrect,
                userId = userId,
                type = type,
                reviewed = false)
            firestore.collection("learningDetail").document(id).set(learningDetail).await()
            id
        } catch (e: Exception) {
            Log.e("FirestoreLearningDetailRepository", "Error creating: ${e.message}")
            null
        }
    }

    suspend fun isExist(questionId: String, userId: String): Boolean {
        return try {
            val querySnapshot = firestore.collection("learningDetail")
                .whereEqualTo("questionId", questionId)
                .whereEqualTo("userId", userId)
                .get()
                .await()
            !querySnapshot.isEmpty
        } catch (e: Exception) {
            Log.e("FirestoreLearningDetailRepository", "Error checking : ${e.message}")
            false
        }
    }

    suspend fun updateLearningDetail(questionId: String, isCorrect: Boolean) : String? {
        return try {
            // Tìm document có trường questionId khớp
            val querySnapshot = firestore.collection("learningDetail")
                .whereEqualTo("questionId", questionId)
                .get()
                .await()

            if (!querySnapshot.isEmpty) {
                // Lấy document đầu tiên (hoặc duyệt nếu cần)
                val document = querySnapshot.documents[0]

                // Cập nhật document
                firestore.collection("learningDetail").document(document.id)
                    .update("isCorrect", isCorrect, "date", System.currentTimeMillis().toString())
                    .await()

                Log.d("FirestoreLearningDetailRepository", "Document updated successfully")
                document.id
            } else {
                Log.d("FirestoreLearningDetailRepository", "No matching document found")
                ""
            }

        } catch (e: Exception) {
            Log.e("FirestoreLearningDetailRepository", "Error updating: ${e.message}")
            ""
        }
    }


    suspend fun getIncorrectGrammarQuestionsWithAnswers(userId: String): List<Pair<FirestoreGrammarQuestion, List<FirestoreGrammarAnswer>>> {
        return try {
            // Lấy các LearningDetail sai của user
            val learningDetailsSnapshot = firestore.collection("learningDetail")
                .whereEqualTo("userId", userId)
                .whereEqualTo("correct", false)
                .whereEqualTo("type", "grammar")
                .get()
                .await()

            val incorrectQuestionIds = learningDetailsSnapshot.documents.mapNotNull { it.getString("questionId") }
            Log.d("Debug", "Incorrect Question IDs: $incorrectQuestionIds")

            if (incorrectQuestionIds.isEmpty()) {
                Log.d("Debug", "No incorrect questions found")
                return emptyList()
            }

            // Lấy thông tin câu hỏi từ bảng grammarQuestion
            val questionsSnapshot = firestore.collection("GrammarQuestion")
                .whereIn(FieldPath.documentId(), incorrectQuestionIds) // Lấy document ID thay vì trường "id"
                .get()
                .await()

            val questions = questionsSnapshot.documents.map { doc ->
                FirestoreGrammarQuestion(
                    id = doc.id,
                    grammarTopicId = doc.getString("grammarTopicId") ?: "",
                    name = doc.getString("name") ?: "",
                    type = doc.getString("type") ?: ""
                )
            }
            Log.d("Debug", "Fetched Questions: $questions")

            // Lấy thông tin đáp án từ bảng grammarAnswer
            val questionAnswerPairs = questions.map { question ->
                val answersSnapshot = firestore.collection("GrammarAnswer")
                    .whereEqualTo("grammarQuestionId", question.id)
                    .get()
                    .await()

                val answers = answersSnapshot.documents.map { doc ->
                    FirestoreGrammarAnswer(
                        id = doc.id,
                        grammarQuestionId = doc.getString("grammarQuestionId") ?: "",
                        answer = doc.getString("answer") ?: "",
                        isCorrect = doc.getBoolean("isCorrect") ?: false
                    )
                }
                Log.d("Debug", "Question: ${question.name}, Answers: $answers")
                question to answers
            }

            questionAnswerPairs
        } catch (e: Exception) {
            Log.e("FirestoreLearningDetailRepository", "Error fetching incorrect questions: ${e.message}")
            emptyList()
        }
    }

}