package com.android_ai.csc13009.app.presentation.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
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
import com.android_ai.csc13009.app.data.local.entity.GrammarLevelEntity
import com.android_ai.csc13009.app.data.repository.GrammarLevelRepository
import com.android_ai.csc13009.app.data.repository.GrammarTopicRepository
import com.android_ai.csc13009.app.domain.models.GrammarTopic
import com.android_ai.csc13009.app.utils.adapter.GrammarTopicAdapter

class GrammarActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private val uiScope = CoroutineScope(Dispatchers.Main)
    private lateinit var grammarLevelRepository: GrammarLevelRepository
    private lateinit var grammarTopicRepository: GrammarTopicRepository

    @SuppressLint("MissingInflatedId")
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

        val levelName = "Beginner"
        //val levelName = intent.getStringExtra("level_name")
        recyclerView = findViewById(R.id.recyclerView)

        val backButton: ImageButton = findViewById(R.id.btnBack)
        backButton.setOnClickListener { finish() }

        // Fetch topics asynchronously based on the level name
        uiScope.launch {
            val topics = getTopicsForLevel(levelName)
            setupRecyclerView(topics)
        }
    }

    // Function to get topics for the given level name
//    private suspend fun getTopicsForLevel(levelName: String?): List<GrammarTopic> {
//        return withContext(Dispatchers.IO) {
//            // Fetch all levels
//            val levels = grammarLevelRepository.getAllLevels()
//
//            // Find the level by its name
//            val level = levels.find { it.name == levelName }
//
//            // Fetch topics for the found level or return an empty list if not found
//            level?.let {
//                grammarTopicRepository.getTopicsByLevel(it.id)  // Fetch topics by level ID
//            } ?: emptyList()
//        }
//    }

    private suspend fun getTopicsForLevel(levelName: String?): List<GrammarTopic> {
        // Mock data cho Grammar Topics
        val mockGrammarTopics = listOf(
            GrammarTopic(id = 1, levelId = 1, name = "Present Tense"),
            GrammarTopic(id = 2, levelId = 1, name = "Past Tense"),
            GrammarTopic(id = 3, levelId = 1, name = "Future Tense"),
            GrammarTopic(id = 4, levelId = 1, name = "Parts of Speech"),
            GrammarTopic(id = 5, levelId = 1, name = "Sentence Structures"),
            GrammarTopic(id = 6, levelId = 1, name = "Subject-Verb Agreement"),
            GrammarTopic(id = 7, levelId = 1, name = "Conditionals"),
            GrammarTopic(id = 8, levelId = 1, name = "Modals"),
            GrammarTopic(id = 9, levelId = 1, name = "Reported Speech"),
            GrammarTopic(id = 10, levelId = 1, name = "Phrasal Verbs"),
        )

        val mockGrammarLevels = listOf(
            GrammarLevelEntity(id = 1, name = "Beginner"),
            GrammarLevelEntity(id = 2, name = "Intermediate"),
            GrammarLevelEntity(id = 3, name = "Advanced")
        )

        return withContext(Dispatchers.IO) {
            val level = mockGrammarLevels.find { it.name == levelName }
            level?.let { mockGrammarTopics.filter { topic -> topic.levelId == level.id } } ?: emptyList()
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



