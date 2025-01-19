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

    suspend fun getSuccessRateByTypeForToday(userId: String, type: String): Float {
        return try {
            val currentDate = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                .format(java.util.Date(System.currentTimeMillis()))


            val querySnapshot = firestore.collection("learningDetail")
                .whereEqualTo("userId", userId)
                .whereEqualTo("type", type)
                .get()
                .await()

            Log.d("FirestoreLearningDetailRepository", "Query result: $querySnapshot")

            if (querySnapshot.isEmpty) return 0f

            val todayQuestions = querySnapshot.documents.filter {
                val dateMillis = it.getString("date")?.toLongOrNull() ?: 0L
                val dateString = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                    .format(java.util.Date(dateMillis))
                dateString == currentDate
            }

            Log.d("FirestoreLearningDetailRepository", "Today's questions: $todayQuestions")

            if (todayQuestions.isEmpty()) return 0f

            val totalQuestions = todayQuestions.size
            val correctAnswers = todayQuestions.count { it.getBoolean("correct") == true }

            Log.d("FirestoreLearningDetailRepository", "Total questions: $totalQuestions, Correct answers: $correctAnswers")

            (correctAnswers.toFloat() / totalQuestions) * 100
        } catch (e: Exception) {
            Log.e("FirestoreLearningDetailRepository", "Error getting today's success rate: ${e.message}")
            0f
        }
    }

    suspend fun getDailyStatistics(userId: String): Map<String, Int> {
        return try {
            val querySnapshot = firestore.collection("learningDetail")
                .whereEqualTo("userId", userId)
                .get()
                .await()

            // Nhóm số câu đã làm theo ngày
            querySnapshot.documents.groupBy { document ->
                val dateMillis = document.getString("date")?.toLongOrNull() ?: 0L
                java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                    .format(java.util.Date(dateMillis))
            }.mapValues { entry ->
                entry.value.size // Đếm số câu đã làm mỗi ngày
            }
        } catch (e: Exception) {
            Log.e("FirestoreLearningDetailRepository", "Error getting daily stats: ${e.message}")
            emptyMap()
        }
    }

    suspend fun getMonthlyStatistics(userId: String): Map<String, Int> {
        return try {
            val querySnapshot = firestore.collection("learningDetail")
                .whereEqualTo("userId", userId)
                .get()
                .await()

            // Nhóm số câu đã làm theo tháng
            querySnapshot.documents.groupBy { document ->
                val dateMillis = document.getString("date")?.toLongOrNull() ?: 0L
                java.text.SimpleDateFormat("yyyy-MM", java.util.Locale.getDefault())
                    .format(java.util.Date(dateMillis))
            }.mapValues { entry ->
                entry.value.size // Đếm số câu đã làm mỗi tháng
            }
        } catch (e: Exception) {
            Log.e("FirestoreLearningDetailRepository", "Error getting monthly stats: ${e.message}")
            emptyMap()
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