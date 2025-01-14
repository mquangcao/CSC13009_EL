package com.android_ai.csc13009.app.presentation.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
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
import com.android_ai.csc13009.app.presentation.fragment.FragmentWordQuestionTypeChat1
import com.android_ai.csc13009.app.presentation.fragment.WordQuestionFragment
import com.google.android.material.button.MaterialButton

class VocabularyWordActivity : AppCompatActivity() {
    private lateinit var btnCheckAnswer : MaterialButton
    private lateinit var dialog: Dialog
    private lateinit var dialogInCorrect: Dialog
    private lateinit var btnConfirmDialog: Button
    private lateinit var btnConfirmDialogIncorrect: Button
    private lateinit var btnClose: ImageButton
    private lateinit var tvQuestion : TextView
    private lateinit var progressBar : ProgressBar
    private var currentQuestion = 0
    private var totalQuestion = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_vocabulary_word)

        loadFragment(FragmentWordQuestionTypeChat1())

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
        if(question.type == "new_word") {
            tvQuestion.text = "Choose the correct word"
//            loadFragment(WordQuestionFragment(question.type, ))
        }
        else {
            tvQuestion.text = "Điền từ còn thếu vào chỗ trống"
            loadFragment(FragmentWordQuestionTypeChat1())
        }
    }

    private fun handleTaskResult(result: String?, questions: ArrayList<Question>?) {
        if (result == "correct") {
            dialog.show()
            upProgressBar()
            btnConfirmDialog.setOnClickListener {
                dialog.dismiss()
                if (currentQuestion < totalQuestion) {
                    val question = questions?.get(currentQuestion)
                    if (question != null) {
                        loadQuestion(question)
                        currentQuestion++
                    }
                } else {
                    val intent = Intent(this, SummaryLearnVocabActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            }
        }
        else {
            dialogInCorrect.show()
            upProgressBar()
            btnConfirmDialogIncorrect.setOnClickListener {
                dialogInCorrect.dismiss()
                if(currentQuestion < totalQuestion) {
                    val question = questions?.get(currentQuestion)
                    if(question != null) {
                        loadQuestion(question)
                        currentQuestion++
                    }
                } else {
                    val intent = Intent(this, SummaryLearnVocabActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }


    }
}