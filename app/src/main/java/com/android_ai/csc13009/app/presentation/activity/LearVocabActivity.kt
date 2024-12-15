package com.android_ai.csc13009.app.presentation.activity

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.domain.models.Lesson
import com.android_ai.csc13009.app.utils.adapter.LessonAdapter

class LearVocabActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lear_vocab)


        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val btnBack = findViewById<ImageView>(R.id.arrow_back)

        val lessons = listOf(
            Lesson(R.drawable.animal, "Lesson 1", 10, 50, true),
            Lesson(R.drawable.animal, "Lesson 2", 15, 80),
            Lesson(R.drawable.animal, "Lesson 3", 20, 100),
            Lesson(R.drawable.animal, "Lesson 1", 10, 50),
            Lesson(R.drawable.animal, "Lesson 2", 15, 80),
            Lesson(R.drawable.animal, "Lesson 3", 20, 100),
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = LessonAdapter(lessons)

        btnBack.setOnClickListener {
            finish()
        }

    }
}