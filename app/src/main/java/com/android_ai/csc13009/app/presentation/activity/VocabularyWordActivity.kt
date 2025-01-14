package com.android_ai.csc13009.app.presentation.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.domain.models.Question
import com.android_ai.csc13009.app.presentation.fragment.FragmentWordQuestionTranslate
import com.android_ai.csc13009.app.presentation.fragment.FragmentWordQuestionTypeChat1
import com.android_ai.csc13009.app.presentation.fragment.WordQuestionFragment
import com.google.android.material.button.MaterialButton

class VocabularyWordActivity : AppCompatActivity() {
    private lateinit var dialog: Dialog
    private lateinit var dialogInCorrect: Dialog
    private lateinit var btnConfirmDialog: Button
    private lateinit var btnConfirmDialogIncorrect: Button
    private lateinit var btnClose: ImageButton
    private lateinit var tvQuestion : TextView
    private lateinit var progressBar : ProgressBar
    private var currentQuestion = 0
    private var correctAnswer = 0
    private var totalQuestion = 0

    private var startTime: Long = 0
    private var elapsedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_vocabulary_word)

        //Hooks
        progressBar = findViewById(R.id.progressBar)
        btnClose = findViewById(R.id.btnClose)
        tvQuestion = findViewById(R.id.tv_question)

        dialog = Dialog(this)
        dialog.setContentView(R.layout.custom_dialog_answer_correct)
        dialog.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawableResource(R.drawable.custom_dialog_bg)
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.setCancelable(false)

        dialogInCorrect = Dialog(this)
        dialogInCorrect.setContentView(R.layout.custom_dialog_answer_uncorrect)
        dialogInCorrect.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogInCorrect.window?.setBackgroundDrawableResource(R.drawable.custom_dialog_bg)
        dialogInCorrect.window?.setGravity(Gravity.BOTTOM)
        dialogInCorrect.setCancelable(false)

        btnConfirmDialog = dialog.findViewById(R.id.btn_continue)
        btnConfirmDialogIncorrect = dialogInCorrect.findViewById(R.id.btn_continue)



        val questions = intent.getSerializableExtra("question") as? ArrayList<Question>

        startTime = System.currentTimeMillis()
        if(questions != null) {
            totalQuestion = questions.size
            progressBar.max = totalQuestion
            currentQuestion = 0
            val question = questions[currentQuestion]
            if (totalQuestion > 0) {
                loadQuestion(question)
                currentQuestion++
            }
        }

        supportFragmentManager.setFragmentResultListener("taskCompleted", this) { requestKey, bundle ->
            if (requestKey == "taskCompleted") {
                // Xử lý kết quả (ở đây có thể là dữ liệu từ Bundle)
                val result = bundle.getString("result")
                handleTaskResult(result, questions)
            }
        }

        btnClose.setOnClickListener {
            finish()
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayout, fragment)
            commit()
        }
    }

    private fun upProgressBar() {
        progressBar.progress = currentQuestion
    }

    private fun loadQuestion(question: Question) {
        when (question.type) {
            "translate" -> {
                tvQuestion.text = getString(R.string.translate_the_word)
                loadFragment(FragmentWordQuestionTranslate(question.question, question.answer))
            }
            "new_word" -> {
                tvQuestion.text = getString(R.string.choose_the_correct_word)
                loadFragment(WordQuestionFragment(question.question, question.answer))
            }
            "meaning" -> {
                tvQuestion.text = getString(R.string.fill_in_the_blank)
                loadFragment(FragmentWordQuestionTypeChat1())
            }
        }

    }

    private fun handleTaskResult(result: String?, questions: ArrayList<Question>?) {
        if (result == "correct") {
            handleNextStep(dialog, questions, btnConfirmDialog)
            correctAnswer++
        }
        else {
            handleNextStep(dialogInCorrect, questions, btnConfirmDialogIncorrect)
        }
    }

    @SuppressLint("DefaultLocale")
    private fun handleNextStep(dialog : Dialog, questions: ArrayList<Question>?, btnConfirm : Button) {
        dialog.show()
        upProgressBar()
        btnConfirm.setOnClickListener {
            dialog.dismiss()
            if (currentQuestion < totalQuestion) {
                val question = questions?.get(currentQuestion)
                if (question != null) {
                    loadQuestion(question)
                    currentQuestion++
                }
            } else {
                val timeText = calTime()

                val intent = Intent(this, SummaryLearnVocabActivity::class.java)
                intent.putExtra("time", timeText)
                intent.putExtra("correctAnswer", (correctAnswer.toDouble() / totalQuestion.toDouble() * 100).toInt())
                startActivity(intent)
                finish()
            }

        }
    }

    @SuppressLint("DefaultLocale")
    private fun calTime() : String {
        elapsedTime = System.currentTimeMillis() - startTime
        val seconds = (elapsedTime / 1000) % 60
        val minutes = (elapsedTime / 1000) / 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}