package com.android_ai.csc13009.app.presentation.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.repository.ListeningTopicRepository
import com.android_ai.csc13009.app.domain.models.ListeningTopic
import com.android_ai.csc13009.app.domain.repository.IListeningTopicRepository
import com.android_ai.csc13009.app.utils.adapter.ListeningLessonAdapter
import com.bumptech.glide.Glide
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import kotlinx.coroutines.launch

class LearnListeningActivity : AppCompatActivity() {
    private lateinit var topicRepository: IListeningTopicRepository
    private lateinit var recyclerView: RecyclerView
    private lateinit var backgroundImage: ImageView
    private lateinit var btnBack: ImageView
    private lateinit var totalLesson : TextView
    private lateinit var tvQuestion : TextView
    private lateinit var circularProgressBar : CircularProgressBar
    private lateinit var tvProgress : TextView
    private lateinit var chapName : TextView

    private var topicId : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_learn_listening)

        init()
        updateUI()
    }

    private fun init() {
        topicRepository = ListeningTopicRepository(this)
        recyclerView = findViewById(R.id.recyclerView)
        btnBack = findViewById(R.id.arrow_back)
        backgroundImage = findViewById(R.id.backgroundImage)
        totalLesson = findViewById(R.id.totalLesson)
        tvQuestion = findViewById(R.id.tv_question)
        tvProgress = findViewById(R.id.tvProgress)
        chapName = findViewById(R.id.chapName)
        circularProgressBar = findViewById(R.id.circularProgressBar)

        topicId = intent.getStringExtra("topicId") ?: ""

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
            val topic = topicRepository.getTopicDetails(topicId)
            // Cập nhật UI sau khi lấy danh sách
            updateUI(topic)
        }

    }

    private fun updateUI(topic: ListeningTopic) {
        chapName.text = topic.title
        recyclerView.adapter = ListeningLessonAdapter(topic.lessons)

        circularProgressBar.progressMax = topic.totalLesson.toFloat()
        circularProgressBar.setProgressWithAnimation(topic.lessonFinished.toFloat())

        val progress = calculateProgression(topic.lessonFinished, topic.totalLesson)
        tvProgress.text = "${progress}%"

        Glide.with(this)
            .load(topic.thumbnailUrl)
            .into(backgroundImage)

        recyclerView.layoutManager = LinearLayoutManager(this)

        totalLesson.text = "${topic.totalLesson} lessons"
        val totalQuestions = topic.lessons.fold(0) { acc, lesson ->
            acc + lesson.totalQuestion
        }
        tvQuestion.text = "$totalQuestions questions"
    }

    private fun calculateProgression(finishedCount: Int, totalCount: Int): Int {
        return finishedCount * 100 / totalCount
    }

}