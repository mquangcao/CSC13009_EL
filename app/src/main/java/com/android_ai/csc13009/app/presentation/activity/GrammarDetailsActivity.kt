package com.android_ai.csc13009.app.presentation.activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.widget.ImageButton
import android.widget.TextView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.local.AppDatabase
import com.android_ai.csc13009.app.data.local.repository.GrammarSubtopicRepository
import com.android_ai.csc13009.app.data.local.repository.GrammarTopicRepository
import com.android_ai.csc13009.app.domain.repository.model.GrammarSubtopic
import com.android_ai.csc13009.app.utils.adapter.GrammarSubtopicAdapter

class GrammarDetailsActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var topicNameTextView: TextView
    private lateinit var grammarSubtopicRepository: GrammarSubtopicRepository
    private lateinit var grammarTopicRepository: GrammarTopicRepository
    private val uiScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grammar_details)

        // Initialize Room Database and DAOs
        val database = AppDatabase.getInstance(this)
        val grammarSubtopicDao = database.grammarSubtopicDao() // Get GrammarSubtopicDao
        val grammarTopicDao = database.grammarTopicDao() // Get GrammarTopicDao

        // Initialize the repositories
        grammarSubtopicRepository = GrammarSubtopicRepository(grammarSubtopicDao)
        grammarTopicRepository = GrammarTopicRepository(grammarTopicDao)

        recyclerView = findViewById(R.id.recyclerView)
        topicNameTextView = findViewById(R.id.textTopicName)

        val backButton: ImageButton = findViewById(R.id.back_button)
        backButton.setOnClickListener { finish() }

        // Get topicId from intent
        val topicId = intent.getIntExtra("topic_id", -1)  // Default value is -1

        if (topicId != -1) {
            uiScope.launch {
                val subtopics = getSubtopicsForTopic(topicId)
                val topicName = getTopicNameById(topicId)  // Fetch the topic name
                topicNameTextView.text = topicName  // Display the topic name
                setupRecyclerView(subtopics)
            }
        } else {
            // Handle case when topicId is invalid
            topicNameTextView.text = "Invalid Topic ID"
        }
    }

    // Function to fetch all subtopics from the repository
    private suspend fun getSubtopicsForTopic(topicId: Int): List<GrammarSubtopic> {
        return withContext(Dispatchers.IO) {
            // You can use the getAllSubtopics method here
            val allSubtopics = grammarSubtopicRepository.getAllSubtopics() // Get all subtopics
            // Filter subtopics by topicId
            allSubtopics.filter { it.topicId == topicId }
        }
    }

    // Function to fetch the topic name by topicId
    private suspend fun getTopicNameById(topicId: Int): String {
        return withContext(Dispatchers.IO) {
            // Use getAllTopics to fetch all topics and filter by topicId
            val allTopics = grammarTopicRepository.getAllTopics() // Get all topics
            val topic = allTopics.find { it.id == topicId }
            topic?.name ?: "Unknown Topic"  // Return the topic name, or a default if not found
        }
    }

    // Function to setup the RecyclerView with the list of subtopics
    private fun setupRecyclerView(subtopics: List<GrammarSubtopic>) {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = GrammarSubtopicAdapter(subtopics)
    }
}




