package com.android_ai.csc13009.app.presentation.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.local.entity.LessonEntity
import com.android_ai.csc13009.app.presentation.adapter.StatisticsLessonDetailAdapter
import com.android_ai.csc13009.app.presentation.adapter.StatisticsVocabularyDetailAdapter

class StatisticsDetailActivity : AppCompatActivity() {
    // note: This is for testing purpose only with sample data
    private lateinit var recyclerView: RecyclerView
    private lateinit var vocabularyList: List<String>
    private lateinit var lessonList: List<LessonEntity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics_detail)

        recyclerView = findViewById(R.id.recyclerViewVocabulary)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Sample data for lists
        vocabularyList = listOf("Vocabulary 1", "Vocabulary 2", "Vocabulary 3", "Vocabulary 4")
        lessonList = listOf(
            LessonEntity(1, "Lesson 1", "Description for Lesson 1", 1, 1, true),
            LessonEntity(2, "Lesson 2", "Description for Lesson 2", 2, 1, false),
            LessonEntity(3, "Lesson 3", "Description for Lesson 3", 3, 2, true)
        )

        // Get the "type" value from Intent
        val type = intent.getStringExtra("type")

        when (type) {
            "correctWords" -> {
                // Display correct vocabulary list
                val adapter = StatisticsVocabularyDetailAdapter(vocabularyList)
                recyclerView.adapter = adapter
                supportActionBar?.title = "Correct Vocabulary"
            }
            "incorrectWords" -> {
                // Display incorrect vocabulary list
                val adapter = StatisticsVocabularyDetailAdapter(vocabularyList)
                recyclerView.adapter = adapter
                supportActionBar?.title = "Incorrect Vocabulary"
            }
            "completedLessons" -> {
                // Display completed lessons list
                val completedLessons = lessonList.filter { it.status }
                val adapter = StatisticsLessonDetailAdapter(completedLessons)
                recyclerView.adapter = adapter
                supportActionBar?.title = "Completed Lessons"
            }
            "pendingLessons" -> {
                // Display pending lessons list
                val pendingLessons = lessonList.filter { !it.status }
                val adapter = StatisticsLessonDetailAdapter(pendingLessons)
                recyclerView.adapter = adapter
                supportActionBar?.title = "Pending Lessons"
            }
            else -> {
                Toast.makeText(this, "Invalid mode", Toast.LENGTH_SHORT).show()
                finish()  // Exit if mode is invalid
                return
            }
        }
    }
}
