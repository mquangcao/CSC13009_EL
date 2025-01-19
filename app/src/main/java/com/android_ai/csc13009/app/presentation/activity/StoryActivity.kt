package com.android_ai.csc13009.app.presentation.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.repository.ChapterRepository
import com.android_ai.csc13009.app.domain.models.Question
import com.android_ai.csc13009.app.domain.repository.IChapterRepository
import com.android_ai.csc13009.app.presentation.fragment.FragmentWordQuestionTranslate
import com.android_ai.csc13009.app.presentation.fragment.FragmentWordQuestionTypeChat1
import com.android_ai.csc13009.app.presentation.fragment.StoryFragment
import com.android_ai.csc13009.app.presentation.fragment.WordQuestionFragment
import kotlinx.coroutines.launch

class StoryActivity : AppCompatActivity() {
    private lateinit var dialog: Dialog
    private lateinit var dialogInCorrect: Dialog
    private lateinit var btnConfirmDialog: Button
    private lateinit var btnConfirmDialogIncorrect: Button
    private lateinit var btnClose: ImageButton
    private lateinit var tvQuestion : TextView
    private lateinit var progressBar : ProgressBar
    private lateinit var headerTitle : CardView
    private var currentQuestion = 0
    private var correctAnswer = 0
    private var totalQuestion = 0

    private var startTime: Long = 0
    private var elapsedTime: Long = 0
    private var lessonId : String = ""

    private var questions : List<Question> = emptyList()

    private lateinit var  chapterRepository : IChapterRepository



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_story)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        chapterRepository = ChapterRepository(this)
        lifecycleScope.launch {
            val chapter = chapterRepository.getChapterDetail("1TNfrw5VT6uL71aZpdr2")
            // Cập nhật UI sau khi lấy danh sách
            questions = chapter.lessons[0].questions
            totalQuestion = questions.size
        }

        initAndHooks()

        loadFragment(StoryFragment())

        supportFragmentManager.setFragmentResultListener("taskCompleted", this) { requestKey, bundle ->
            if (requestKey == "taskCompleted") {
                // Xử lý kết quả (ở đây có thể là dữ liệu từ Bundle)
                val story = bundle.getString("story")
                val result = bundle.getString("result")
                val questionId = bundle.getString("questionId")

                if (story == "completed") {
                    headerTitle.visibility = View.VISIBLE
                    val question = questions[currentQuestion]
                    if (totalQuestion > 0) {
                        progressBar.max = totalQuestion
                        startTime = System.currentTimeMillis()
                        loadQuestion(question)
                        currentQuestion++
                    }
                } else {
                    handleTaskResult(result, questions)
                }
            }
        }


        btnClose.setOnClickListener {
            finish()
        }
    }

    private fun handleTaskResult(result: String?, questions: List<Question>) {
        if (result == "correct") {
            handleNextStep(dialog, questions, btnConfirmDialog)
            correctAnswer++
        }
        else {
            handleNextStep(dialogInCorrect, questions, btnConfirmDialogIncorrect)
        }
    }

    private fun handleNextStep(dialog: Dialog, questions: List<Question>, btnConfirm: Button) {
        dialog.show()
        upProgressBar()
        btnConfirm.setOnClickListener {
            dialog.dismiss()
            if (currentQuestion < totalQuestion) {
                val question = questions.get(currentQuestion)
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

    private fun initAndHooks() {
        //Hooks
        progressBar = findViewById(R.id.progressBar)
        btnClose = findViewById(R.id.btnClose)
        tvQuestion = findViewById(R.id.tv_question)
        headerTitle = findViewById(R.id.header_title)

        headerTitle.visibility = View.GONE

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
    }

    private fun loadQuestion(question: Question) {
        when (question.type) {
            "translate" -> {
                tvQuestion.text = getString(R.string.translate_the_word)
                loadFragment(FragmentWordQuestionTranslate(question.id ,question.question, question.answer))
            }
            "new_word" -> {
                tvQuestion.text = getString(R.string.choose_the_correct_word)
                loadFragment(WordQuestionFragment(question.id , question.question, question.answer))
            }
            "meaning" -> {
                tvQuestion.text = getString(R.string.fill_in_the_blank)
                loadFragment(FragmentWordQuestionTypeChat1(question.id , question.question, question.answer))
            }
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
}