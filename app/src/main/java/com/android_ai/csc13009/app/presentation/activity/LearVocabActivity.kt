package com.android_ai.csc13009.app.presentation.activity

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.repository.ChapterRepository
import com.android_ai.csc13009.app.domain.repository.IChapterRepository
import com.android_ai.csc13009.app.utils.adapter.LessonAdapter
import com.bumptech.glide.Glide

class LearVocabActivity : AppCompatActivity() {
    private lateinit var IChapterRepository : IChapterRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lear_vocab)

        IChapterRepository = ChapterRepository()

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val btnBack = findViewById<ImageView>(R.id.arrow_back)
        val backgroundImage = findViewById<ImageView>(R.id.backgroundImage)


        val chapter = IChapterRepository.getChapterDetail(1)
        recyclerView.adapter = LessonAdapter(chapter.lessons)

        Glide.with(this)
            .load(chapter.thumbnailUrl)
            .into(backgroundImage)

        recyclerView.layoutManager = LinearLayoutManager(this)

        btnBack.setOnClickListener {
            finish()
        }

    }
}