package com.android_ai.csc13009.app.presentation.activity

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.domain.models.Story
import com.android_ai.csc13009.app.utils.adapter.ListStoriesAdapter

class ListStoryActivity : AppCompatActivity() {
    private lateinit var recyclerView : RecyclerView
    private lateinit var btnBack : ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_list_story)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        hooks()
        setup()
        btnBack.setOnClickListener { finish() }
    }

    private fun sampleStories() : List<Story> {
        return listOf(
            Story("1", "Story 1", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRyygsJpwRVl2HKhLbCbDmJODbpWMviZSCykA&s"),
            Story("1", "Story 2", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRyygsJpwRVl2HKhLbCbDmJODbpWMviZSCykA&s"),
            Story("1", "Story 3", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRyygsJpwRVl2HKhLbCbDmJODbpWMviZSCykA&s"),
        )
    }

    private fun setup() {
        recyclerView.adapter = ListStoriesAdapter(sampleStories())
        recyclerView.layoutManager = GridLayoutManager(this, 2)
    }

    private fun hooks() {
        recyclerView = findViewById(R.id.recyclerView)
        btnBack = findViewById(R.id.btnBack)
    }
}