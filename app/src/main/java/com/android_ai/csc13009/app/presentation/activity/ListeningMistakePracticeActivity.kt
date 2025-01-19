package com.android_ai.csc13009.app.presentation.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.local.AppDatabase
import com.android_ai.csc13009.app.data.local.entity.LearningDetailEntity
import com.android_ai.csc13009.app.data.remote.repository.FirestoreLearningDetailRepository
import com.android_ai.csc13009.app.domain.models.ListeningQuestion
import com.android_ai.csc13009.app.presentation.fragment.ListeningQuestionFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class ListeningMistakePracticeActivity : AppCompatActivity() {
    private lateinit var dialog: Dialog
    private lateinit var dialogInCorrect: Dialog
    private lateinit var dialogIncorrectAnswerCorrection: TextView
    private lateinit var btnConfirmDialog: Button
    private lateinit var btnConfirmDialogIncorrect: Button
    private lateinit var btnClose: ImageButton
    private lateinit var tvQuestion: TextView
    private lateinit var progressBar: ProgressBar

    private var startTime: Long = 0
    private var elapsedTime: Long = 0
    private var question: ListeningQuestion? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_listening_question)

        // Hooks
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
        dialogIncorrectAnswerCorrection = dialogInCorrect.findViewById(R.id.tv_content)

        btnConfirmDialog = dialog.findViewById(R.id.btn_continue)
        btnConfirmDialogIncorrect = dialogInCorrect.findViewById(R.id.btn_continue)

        question = intent.getSerializableExtra("question") as? ListeningQuestion

        startTime = System.currentTimeMillis()

        if (question != null) {
            loadQuestion(question!!)
        }

        supportFragmentManager.setFragmentResultListener("taskCompleted", this) { requestKey, bundle ->
            if (requestKey == "taskCompleted") {
                val result = bundle.getString("result")
                val questionId = bundle.getString("questionId")

                handleTaskResult(result, question)
                handleSaveLearningDetail(result == "correct", questionId)
            }
        }

        btnClose.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }
    }

    private fun handleSaveLearningDetail(isCorrect: Boolean, questionId: String?) {
        lifecycleScope.launch {
            val repository = FirestoreLearningDetailRepository(FirebaseFirestore.getInstance())
            val database = AppDatabase.getInstance(this@ListeningMistakePracticeActivity).learningDetailDao()

            val userId = getUserId()
            if (repository.isExist(questionId ?: "", userId)) {
                val learningDetailId = repository.updateLearningDetail(userId, questionId ?: "", isCorrect)

                val learningDetail = LearningDetailEntity(
                    id = learningDetailId ?: "",
                    date = System.currentTimeMillis().toString(),
                    questionId = questionId ?: "",
                    isCorrect = isCorrect,
                    userId = userId,
                    type = "listening",
                    isReviewed = true
                )

                database.insertLearningDetail(learningDetail)

            } else {
                val learningDetailId = repository.createLearningDetail(userId, questionId ?: "", isCorrect, "listening")

                val learningDetail = LearningDetailEntity(
                    id = learningDetailId ?: "",
                    date = System.currentTimeMillis().toString(),
                    questionId = questionId ?: "",
                    isCorrect = isCorrect,
                    userId = userId,
                    type = "listening",
                    isReviewed = true
                )

                database.insertLearningDetail(learningDetail)
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayout, fragment)
            commit()
        }
    }

    private fun loadQuestion(question: ListeningQuestion) {
        when (question.type) {
            "pronunciation" -> {
                tvQuestion.text = getString(R.string.choose_the_correct_word)
            }
            "stressing" -> {
                tvQuestion.text = getString(R.string.find_the_different_from_the_other)
            }
            "sound" -> {
                tvQuestion.text = getString(R.string.choose_the_correct_word)
            }
            "conversation" -> {
                tvQuestion.text = getString(R.string.choose_the_correct_information)
            }
        }

        loadFragment(ListeningQuestionFragment(question.id, question.question, question.answer, question.audioTranscript))
    }

    private fun handleTaskResult(result: String?, question: ListeningQuestion?) {
        if (result == "correct") {
            handleCompletion(dialog, btnConfirmDialog)
        } else {
            val rightAnswer = question?.answer?.find { it.isCorrect }
            dialogIncorrectAnswerCorrection.text = rightAnswer?.text ?: ""

            handleCompletion(dialogInCorrect, btnConfirmDialogIncorrect)
        }
    }

    private fun handleCompletion(dialog: Dialog, btnConfirm: Button) {
        dialog.show()
        btnConfirm.setOnClickListener {
            dialog.dismiss()
            val timeText = calTime()

//            val intent = Intent(this, SummaryLearnVocabActivity::class.java)
//            intent.putExtra("time", timeText)
//            intent.putExtra("correctAnswer", if (dialog == this.dialog) 100 else 0)
//            startActivity(intent)
            setResult(RESULT_OK)
            finish()
        }
    }

    private fun getUserId(): String {
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show()
            return ""
        }
        return currentUser.uid
    }

    @SuppressLint("DefaultLocale")
    private fun calTime(): String {
        elapsedTime = System.currentTimeMillis() - startTime
        val seconds = (elapsedTime / 1000) % 60
        val minutes = (elapsedTime / 1000) / 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}
