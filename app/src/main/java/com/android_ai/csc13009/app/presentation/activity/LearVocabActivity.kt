package com.android_ai.csc13009.app.presentation.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.repository.ChapterRepository
import com.android_ai.csc13009.app.domain.models.Chapter
import com.android_ai.csc13009.app.domain.models.Lesson
import com.android_ai.csc13009.app.domain.repository.IChapterRepository
import com.android_ai.csc13009.app.utils.adapter.LessonAdapter
import com.bumptech.glide.Glide
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import kotlinx.coroutines.launch
import kotlin.math.log

class LearVocabActivity : AppCompatActivity() {
    private lateinit var IChapterRepository : IChapterRepository
    private lateinit var recyclerView: RecyclerView
    private lateinit var backgroundImage: ImageView
    private lateinit var btnBack: ImageView
    private lateinit var totalLesson : TextView
    private lateinit var tvQuestion : TextView
    private lateinit var circularProgressBar : CircularProgressBar
    private lateinit var tvProgress : TextView

    private var chapterId : String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lear_vocab)

        IChapterRepository = ChapterRepository(this)

        recyclerView = findViewById(R.id.recyclerView)
        btnBack = findViewById(R.id.arrow_back)
        backgroundImage = findViewById(R.id.backgroundImage)
        totalLesson = findViewById(R.id.totalLesson)
        tvQuestion = findViewById(R.id.tv_question)
        tvProgress = findViewById(R.id.tvProgress)
        circularProgressBar = findViewById(R.id.circularProgressBar)


        chapterId = intent.getStringExtra("chapterId") ?: ""

        updateUI()

        btnBack.setOnClickListener {
            finish()
        }

    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    private fun updateUI() {
        lifecycleScope.launch {
            val chapter = IChapterRepository.getChapterDetail(chapterId)
            // Cập nhật UI sau khi lấy danh sách
            updateUI(chapter)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(chapter: Chapter) {
        recyclerView.adapter = LessonAdapter(chapter.lessons)

        circularProgressBar.progressMax = chapter.totalLesson.toFloat()
        circularProgressBar.setProgressWithAnimation(chapter.lessonFinished.toFloat())

        tvProgress.text = "${(chapter.lessonFinished.toFloat() / chapter.totalLesson.toFloat() * 100).toInt()}%"

        Glide.with(this)
            .load(chapter.thumbnailUrl)
            .into(backgroundImage)

        recyclerView.layoutManager = LinearLayoutManager(this)

        totalLesson.text = "${chapter.totalLesson} lessons"
        val totalQuestions = chapter.lessons.fold(0) { acc, lesson ->
            acc + lesson.totalQuestion
        }
        tvQuestion.text = "$totalQuestions questions"
    }
}