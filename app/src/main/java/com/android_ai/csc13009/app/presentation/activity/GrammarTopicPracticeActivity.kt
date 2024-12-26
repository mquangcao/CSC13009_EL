package com.android_ai.csc13009.app.presentation.activity

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.domain.models.GrammarAnswer
import com.android_ai.csc13009.app.domain.models.GrammarQuestion
import com.android_ai.csc13009.app.presentation.fragment.GrammarQuestionFragment

class GrammarTopicPracticeActivity : AppCompatActivity() {

    private val questions = listOf(
        GrammarQuestion(
            grammarQuestionId = 1,
            grammarTopicId = 1,
            name = "Complete the sentence: 'He ___ (study) right now.'",
            type = "multiple-choice"
        ),
        GrammarQuestion(
            grammarQuestionId = 2,
            grammarTopicId = 1,
            name = "Complete the sentence: 'She ___ (finish) her homework already.'",
            type = "multiple-choice"
        ),
        GrammarQuestion(
            grammarQuestionId = 3,
            grammarTopicId = 1,
            name = "Complete the sentence: 'They ___ (play) football when it started to rain.'",
            type = "multiple-choice"
        )
    )

    private val answers = listOf(
        listOf(
            GrammarAnswer(grammarAnswerId = 1, grammarQuestionId = 1, answer = "is studying", isCorrect = true),
            GrammarAnswer(grammarAnswerId = 2, grammarQuestionId = 1, answer = "studies", isCorrect = false),
            GrammarAnswer(grammarAnswerId = 3, grammarQuestionId = 1, answer = "studied", isCorrect = false),
            GrammarAnswer(grammarAnswerId = 4, grammarQuestionId = 1, answer = "has studied", isCorrect = false)
        ),
        listOf(
            GrammarAnswer(grammarAnswerId = 5, grammarQuestionId = 2, answer = "has finished", isCorrect = true),
            GrammarAnswer(grammarAnswerId = 6, grammarQuestionId = 2, answer = "finished", isCorrect = false),
            GrammarAnswer(grammarAnswerId = 7, grammarQuestionId = 2, answer = "finishes", isCorrect = false),
            GrammarAnswer(grammarAnswerId = 8, grammarQuestionId = 2, answer = "is finishing", isCorrect = false)
        ),
        listOf(
            GrammarAnswer(grammarAnswerId = 9, grammarQuestionId = 3, answer = "were playing", isCorrect = true),
            GrammarAnswer(grammarAnswerId = 10, grammarQuestionId = 3, answer = "are playing", isCorrect = false),
            GrammarAnswer(grammarAnswerId = 11, grammarQuestionId = 3, answer = "played", isCorrect = false),
            GrammarAnswer(grammarAnswerId = 12, grammarQuestionId = 3, answer = "have played", isCorrect = false)
        )
    )


    private var currentQuestionIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grammar_topic_practice)

        // Get grammarTopicId from intent
        val grammarTopicId = intent.getIntExtra("grammarTopicId", -1)
        Log.d("GrammarTopicPracticeActivity", "Received grammarTopicId: $grammarTopicId")

        if (grammarTopicId != -1) {
            showNextQuestion(grammarTopicId)
        } else {
            Toast.makeText(this, "Invalid Topic ID", Toast.LENGTH_SHORT).show()
        }

        val closeButton : ImageButton = findViewById(R.id.btnClose)
        closeButton.setOnClickListener {
            finish()
        }
    }

    private fun showNextQuestion(grammarTopicId: Int) {
        // Lọc các câu hỏi dựa trên grammarTopicId
        val filteredQuestions = questions.filter { it.grammarTopicId == grammarTopicId }

        if (currentQuestionIndex < filteredQuestions.size) {
            val question = filteredQuestions[currentQuestionIndex]

            // Lấy danh sách đáp án dựa trên grammarQuestionId
            val currentAnswers = answers.firstOrNull { it.any { answer -> answer.grammarQuestionId == question.grammarQuestionId } }
                ?: emptyList()

            // Tạo và hiển thị fragment với câu hỏi và danh sách đáp án
            val fragment = GrammarQuestionFragment.newInstance(question, currentAnswers) { _ ->
                currentQuestionIndex++
                showNextQuestion(grammarTopicId)
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit()
        } else {
            // Hoàn thành tất cả câu hỏi
            Toast.makeText(this, "You've completed all questions!", Toast.LENGTH_SHORT).show()
        }
    }

}

