package com.android_ai.csc13009.app.presentation.activity

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.repository.StoryRepository
import com.android_ai.csc13009.app.domain.models.Story
import com.android_ai.csc13009.app.utils.adapter.ListStoriesAdapter
import kotlinx.coroutines.launch

class ListStoryActivity : AppCompatActivity() {
    private lateinit var recyclerView : RecyclerView
    private lateinit var btnBack : ImageButton

    private lateinit var storyRepository: StoryRepository


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

    private suspend fun sampleStories() : List<Story> {
        return storyRepository.getAllStory()
    }

    private fun setup() {
        storyRepository = StoryRepository(this)
        lifecycleScope.launch {
            recyclerView.adapter = ListStoriesAdapter(sampleStories())
            recyclerView.layoutManager = GridLayoutManager(this@ListStoryActivity, 2)
        }


    }

    private fun hooks() {
        recyclerView = findViewById(R.id.recyclerView)
        btnBack = findViewById(R.id.btnBack)
    }
}