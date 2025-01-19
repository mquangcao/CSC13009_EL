package com.android_ai.csc13009.app.presentation.activity

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.remote.repository.FirestoreGrammarAnswerRepository
import com.android_ai.csc13009.app.data.remote.repository.FirestoreLearningDetailRepository
import com.android_ai.csc13009.app.data.repository.GrammarAnswerRepository
import com.android_ai.csc13009.app.domain.models.GrammarQuestion
import com.android_ai.csc13009.app.presentation.fragment.GrammarQuestionFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class GrammarMistakePracticeActivity : AppCompatActivity() {

    private lateinit var grammarAnswersRepository: GrammarAnswerRepository
    private lateinit var firestoreLearningDetailRepository: FirestoreLearningDetailRepository
    private var currentUserId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grammar_topic_practice) // Reusing the same layout

        val firestore = FirebaseFirestore.getInstance()
        val firebaseAuth = FirebaseAuth.getInstance()

        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show()
            return
        }
        currentUserId = currentUser.uid

        val firestoreGrammarAnswerRepository = FirestoreGrammarAnswerRepository(firestore)
        firestoreLearningDetailRepository = FirestoreLearningDetailRepository(firestore)
        grammarAnswersRepository = GrammarAnswerRepository(firestoreGrammarAnswerRepository)

        val progressBar: ProgressBar = findViewById(R.id.progressBar)

        val question = intent.getSerializableExtra("question") as? GrammarQuestion
        if (question != null) {
            showQuestion(question, progressBar)
        } else {
            Toast.makeText(this, "No question provided.", Toast.LENGTH_SHORT).show()
        }

        val closeButton: ImageButton = findViewById(R.id.btnClose)
        closeButton.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }
    }

    private fun showQuestion(question: GrammarQuestion, progressBar: ProgressBar) {
        lifecycleScope.launch {
            try {
                val currentAnswers = grammarAnswersRepository.getAnswersByQuestionId(question.id)

                val fragment = GrammarQuestionFragment.newInstance(question, currentAnswers) { isCorrect ->

                    lifecycleScope.launch {
                        currentUserId?.let { userId ->
                            val isExist = firestoreLearningDetailRepository.isExist(question.id, userId)
                            if (isExist) {
                                firestoreLearningDetailRepository.updateLearningDetail(userId, question.id, isCorrect)
                            } else {
                                firestoreLearningDetailRepository.createLearningDetail(
                                    userId = userId,
                                    questionId = question.id,
                                    isCorrect = isCorrect,
                                    type = "grammar"
                                )
                            }
                        } ?: run {
                            Toast.makeText(this@GrammarMistakePracticeActivity, "User ID is null", Toast.LENGTH_SHORT).show()
                        }
                    }

                    progressBar.progress = 100 // Since it's only one question, set progress to 100%
                    //Toast.makeText(this@GrammarMistakePracticeActivity, "Practice completed.", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)
                    finish()
                }

                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .commit()

            } catch (e: Exception) {
                Toast.makeText(this@GrammarMistakePracticeActivity, "Error loading question.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
