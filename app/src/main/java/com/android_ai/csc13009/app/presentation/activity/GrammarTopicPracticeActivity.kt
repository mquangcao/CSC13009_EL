package com.android_ai.csc13009.app.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.remote.repository.FirestoreGrammarAnswerRepository
import com.android_ai.csc13009.app.data.remote.repository.FirestoreGrammarQuestionRepository
import com.android_ai.csc13009.app.data.repository.GrammarAnswerRepository
import com.android_ai.csc13009.app.data.repository.GrammarQuestionRepository
import com.android_ai.csc13009.app.presentation.fragment.GrammarQuestionFragment
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class GrammarTopicPracticeActivity : AppCompatActivity() {

    private lateinit var grammarQuestionRepository: GrammarQuestionRepository
    private lateinit var grammarAnswersRepository: GrammarAnswerRepository

    private var currentQuestionIndex = 0
    private var totalQuestions = 0
    private var correctAnswersCount = 0
    private var incorrectAnswersCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grammar_topic_practice)

        val firestore = FirebaseFirestore.getInstance()

        // Initialize repositories
        val firestoreGrammarQuestionRepository = FirestoreGrammarQuestionRepository(firestore)
        val firestoreGrammarAnswerRepository = FirestoreGrammarAnswerRepository(firestore)
        grammarQuestionRepository = GrammarQuestionRepository(firestoreGrammarQuestionRepository)
        grammarAnswersRepository = GrammarAnswerRepository(firestoreGrammarAnswerRepository)

        val progressBar: ProgressBar = findViewById(R.id.progressBar)

        // Get grammarTopicId from intent
        val grammarTopicId = intent.getStringExtra("grammarTopicId")
        Log.d("GrammarTopicPracticeActivity", "Received grammarTopicId: $grammarTopicId")

        if (!grammarTopicId.isNullOrEmpty()) {
            showNextQuestion(grammarTopicId, progressBar)
        } else {
            Toast.makeText(this, "Invalid Topic ID", Toast.LENGTH_SHORT).show()
        }

        val closeButton: ImageButton = findViewById(R.id.btnClose)
        closeButton.setOnClickListener {
            finish()
        }
    }

    private fun showNextQuestion(grammarTopicId: String, progressBar: ProgressBar) {
        lifecycleScope.launch {
            try {
                // Fetch all questions for the given topicId
                val questions = grammarQuestionRepository.getQuestionsByTopicId(grammarTopicId)
                totalQuestions = questions.size // Save the total number of questions
                Log.d("totalQuestions", totalQuestions.toString())

                if (currentQuestionIndex < questions.size) {
                    val question = questions[currentQuestionIndex]

                    // Fetch answers for the current question
                    val currentAnswers = grammarAnswersRepository.getAnswersByQuestionId(question.id)
                    Log.d("currentAnswers", currentAnswers.toString())

                    // Create and display the fragment with the question and answers
                    val fragment = GrammarQuestionFragment.newInstance(question, currentAnswers) { isCorrect ->
                        if (isCorrect) correctAnswersCount++ else incorrectAnswersCount++
                        currentQuestionIndex++
                        updateProgressBar(progressBar)
                        showNextQuestion(grammarTopicId, progressBar)
                    }

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, fragment)
                        .commit()
                } else {
                    // Navigate to summary screen once all questions have been answered
                    val intent = Intent(this@GrammarTopicPracticeActivity, SummaryLearnGrammarActivity::class.java).apply {
                        putExtra("CORRECT_ANSWERS", correctAnswersCount)
                        putExtra("INCORRECT_ANSWERS", incorrectAnswersCount)
                    }
                    startActivity(intent)
                    finish()
                }
            } catch (e: Exception) {
                Log.e("GrammarTopicPracticeActivity", "Error fetching questions or answers", e)
                Toast.makeText(this@GrammarTopicPracticeActivity, "Error loading questions.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateProgressBar(progressBar: ProgressBar) {
        val progressPercentage = ((currentQuestionIndex).toFloat() / totalQuestions) * 100
        progressBar.progress = progressPercentage.toInt()
    }
}
