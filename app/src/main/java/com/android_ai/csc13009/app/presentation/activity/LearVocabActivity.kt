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


        recyclerView.layoutManager = LinearLayoutManager(this)

        btnBack.setOnClickListener {
            finish()
        }

    }
}