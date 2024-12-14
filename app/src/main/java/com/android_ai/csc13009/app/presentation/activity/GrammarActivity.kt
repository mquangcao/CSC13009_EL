package com.android_ai.csc13009.app.presentation.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.widget.ImageButton
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.local.AppDatabase
import com.android_ai.csc13009.app.data.local.repository.GrammarLevelRepository
import com.android_ai.csc13009.app.data.local.repository.GrammarTopicRepository
import com.android_ai.csc13009.app.domain.repository.model.GrammarTopic
import com.android_ai.csc13009.app.presentation.adapter.GrammarTopicAdapter

class GrammarActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private val uiScope = CoroutineScope(Dispatchers.Main)
    private lateinit var grammarLevelRepository: GrammarLevelRepository
    private lateinit var grammarTopicRepository: GrammarTopicRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grammar)

        // Initialize Room Database and DAOs
        val database = AppDatabase.getInstance(this)
        val grammarLevelDao = database.grammarLevelDao() // Get GrammarLevelDao
        val grammarTopicDao = database.grammarTopicDao() // Get GrammarTopicDao

        // Initialize the repository with all required DAOs
        grammarLevelRepository = GrammarLevelRepository(grammarLevelDao)
        grammarTopicRepository = GrammarTopicRepository(grammarTopicDao)

        val levelName = intent.getStringExtra("level_name")  // Receive the level name from the intent
        recyclerView = findViewById(R.id.recyclerView)

        val backButton: ImageButton = findViewById(R.id.back_button)
        backButton.setOnClickListener { finish() }

        // Fetch topics asynchronously based on the level name
        uiScope.launch {
            val topics = getTopicsForLevel(levelName)
            setupRecyclerView(topics)
        }
    }

    // Function to get topics for the given level name
    private suspend fun getTopicsForLevel(levelName: String?): List<GrammarTopic> {
        return withContext(Dispatchers.IO) {
            // Fetch all levels
            val levels = grammarLevelRepository.getAllLevels()

            // Find the level by its name
            val level = levels.find { it.name == levelName }

            // Fetch topics for the found level or return an empty list if not found
            level?.let {
                grammarTopicRepository.getTopicsByLevel(it.id)  // Fetch topics by level ID
            } ?: emptyList()
        }
    }

    // Function to set up the RecyclerView with the topics list
    private fun setupRecyclerView(topics: List<GrammarTopic>) {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = GrammarTopicAdapter(topics) { topic ->
            // On topic click, open GrammarDetailsActivity and pass the topic name
            val intent = Intent(this, GrammarDetailsActivity::class.java)
            intent.putExtra("topic_name", topic.name)
            intent.putExtra("topic_id", topic.id)  // Pass the topic ID too for detailed fetching
            startActivity(intent)
        }
    }
}

