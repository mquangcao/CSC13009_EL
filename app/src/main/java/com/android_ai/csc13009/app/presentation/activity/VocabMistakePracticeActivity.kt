package com.android_ai.csc13009.app.presentation.activity

import android.app.Dialog
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
import com.android_ai.csc13009.app.domain.models.Question
import com.android_ai.csc13009.app.presentation.fragment.FragmentWordQuestionTranslate
import com.android_ai.csc13009.app.presentation.fragment.FragmentWordQuestionTypeChat1
import com.android_ai.csc13009.app.presentation.fragment.WordQuestionFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class VocabMistakePracticeActivity : AppCompatActivity() {
    private lateinit var dialog: Dialog
    private lateinit var dialogInCorrect: Dialog
    private lateinit var btnConfirmDialog: Button
    private lateinit var btnConfirmDialogIncorrect: Button
    private lateinit var btnClose: ImageButton
    private lateinit var tvQuestion: TextView
    private lateinit var progressBar: ProgressBar
    private var isQuestionAnswered = false
    private lateinit var question: Question

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_vocabulary_word)

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

        btnConfirmDialog = dialog.findViewById(R.id.btn_continue)
        btnConfirmDialogIncorrect = dialogInCorrect.findViewById(R.id.btn_continue)

        // Get the question from the intent
        question = intent.getSerializableExtra("question") as Question
        loadQuestion(question)

        supportFragmentManager.setFragmentResultListener("taskCompleted", this) { requestKey, bundle ->
            if (requestKey == "taskCompleted") {
                val result = bundle.getString("result")
                handleTaskResult(result, question)
            }
        }

        btnClose.setOnClickListener {
            finish()
        }
    }

    private fun loadQuestion(question: Question) {
        when (question.type) {
            "translate" -> {
                tvQuestion.text = getString(R.string.translate_the_word)
                loadFragment(FragmentWordQuestionTranslate(question.id, question.question, question.answer))
            }
            "new_word" -> {
                tvQuestion.text = getString(R.string.choose_the_correct_word)
                loadFragment(WordQuestionFragment(question.id, question.question, question.answer))
            }
            "meaning" -> {
                tvQuestion.text = getString(R.string.fill_in_the_blank)
                loadFragment(FragmentWordQuestionTypeChat1(question.id, question.question, question.answer))
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayout, fragment)
            commit()
        }
    }

    private fun handleTaskResult(result: String?, question: Question) {
        if (isQuestionAnswered) return // Prevent multiple submissions
        isQuestionAnswered = true

        if (result == "correct") {
            handleNextStep(dialog, btnConfirmDialog, true, question)
        } else {
            handleNextStep(dialogInCorrect, btnConfirmDialogIncorrect, false, question)
        }
    }

    private fun handleNextStep(dialog: Dialog, btnConfirm: Button, isCorrect: Boolean, question: Question) {
        dialog.show()
        btnConfirm.setOnClickListener {
            dialog.dismiss()
            lifecycleScope.launch {
                saveLearningDetail(isCorrect, question)
            }

//            val intent = Intent(this, SummaryLearnVocabActivity::class.java)
//            intent.putExtra("correctAnswer", if (isCorrect) 100 else 0)
//            startActivity(intent)
            finish()
        }
    }

    private suspend fun saveLearningDetail(isCorrect: Boolean, question: Question) {
        val repository = FirestoreLearningDetailRepository(FirebaseFirestore.getInstance())
        val database = AppDatabase.getInstance(this@VocabMistakePracticeActivity).learningDetailDao()

        val userId = getUserId()
        val learningDetailId = if (repository.isExist(question.id, userId)) {
            repository.updateLearningDetail(question.id, isCorrect)
        } else {
            repository.createLearningDetail(userId, question.id, isCorrect, "vocabulary")
        }

        val learningDetail = LearningDetailEntity(
            id = learningDetailId ?: "",
            date = System.currentTimeMillis().toString(),
            questionId = question.id,
            isCorrect = isCorrect,
            userId = userId,
            type = "vocabulary",
            isReviewed = true // Mark as reviewed since this is a mistake practice
        )

        database.insertLearningDetail(learningDetail)
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
}
