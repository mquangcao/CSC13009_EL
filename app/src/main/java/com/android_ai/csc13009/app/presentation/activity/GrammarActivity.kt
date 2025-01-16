package com.android_ai.csc13009.app.presentation.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.widget.ImageButton
import android.widget.ProgressBar
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.remote.repository.FirestoreGrammarLevelRepository
import com.android_ai.csc13009.app.data.remote.repository.FirestoreGrammarTopicRepository
import com.android_ai.csc13009.app.data.repository.GrammarLevelRepository
import com.android_ai.csc13009.app.data.repository.GrammarTopicRepository
import com.android_ai.csc13009.app.domain.models.GrammarTopic
import com.android_ai.csc13009.app.utils.adapter.GrammarTopicAdapter
import com.google.firebase.firestore.FirebaseFirestore

class GrammarActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private val uiScope = CoroutineScope(Dispatchers.Main)

    private lateinit var grammarTopicRepository: GrammarTopicRepository
    private lateinit var grammarLevelRepository: GrammarLevelRepository

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grammar)

        // Initialize Firestore and Repository
        val firestore = FirebaseFirestore.getInstance()
        val firestoreGrammarTopicRepository = FirestoreGrammarTopicRepository(firestore)
        val firestoreGrammarLevelRepository = FirestoreGrammarLevelRepository(firestore)
        grammarTopicRepository = GrammarTopicRepository(firestoreGrammarTopicRepository)
        grammarLevelRepository = GrammarLevelRepository(firestoreGrammarLevelRepository)

        val levelName = "Beginner"
        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)

        val backButton: ImageButton = findViewById(R.id.btnBack)
        backButton.setOnClickListener { finish() }

        // Show ProgressBar and hide RecyclerView while loading
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE

        // Fetch topics asynchronously based on the level name
        uiScope.launch {
            val topics = getTopicsForLevel(levelName)
            Log.d("topics:", topics.toString())
            setupRecyclerView(topics)

            // Hide ProgressBar and show RecyclerView when loading is done
            progressBar.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }

    // Function to fetch topics based on the level name
    private suspend fun getTopicsForLevel(levelName: String?): List<GrammarTopic> {
        return withContext(Dispatchers.IO) {
            if (levelName == null) return@withContext emptyList()

            // Get level by name
            val level = grammarLevelRepository.getLevelByName(levelName)
            Log.d("level:", level.toString())

            // Retrieve level ID
            val currentLevelId = level?.id

            // Fetch topics by level ID if available
            currentLevelId?.let { grammarTopicRepository.getTopicsByLevel(it) } ?: emptyList()
        }
    }

    // Function to set up the RecyclerView with the topics list
    private fun setupRecyclerView(topics: List<GrammarTopic>) {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = GrammarTopicAdapter(topics) { topic ->
            // On topic click, open GrammarDetailsActivity and pass the topic name
            val intent = Intent(this, GrammarDetailsActivity::class.java)
            intent.putExtra("topic_name", topic.name)

            Log.d("topic_name", topic.name)
            intent.putExtra("topic_id", topic.id)  // Pass the topic ID too for detailed fetching
            Log.d("topic_id", topic.id)
            startActivity(intent)
        }
    }
}




