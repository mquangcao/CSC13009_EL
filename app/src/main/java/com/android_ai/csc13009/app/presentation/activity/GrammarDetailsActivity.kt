package com.android_ai.csc13009.app.presentation.activity
import android.os.Bundle
import android.util.Log
import android.widget.Button
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
import com.android_ai.csc13009.app.data.remote.repository.FirestoreGrammarSubtopicRepository
import com.android_ai.csc13009.app.data.remote.repository.FirestoreGrammarTopicRepository
import com.android_ai.csc13009.app.data.repository.GrammarSubtopicRepository
import com.android_ai.csc13009.app.data.repository.GrammarTopicRepository
import com.android_ai.csc13009.app.domain.models.GrammarSubtopic
import com.android_ai.csc13009.app.utils.adapter.GrammarSubtopicAdapter
import com.google.firebase.firestore.FirebaseFirestore

class GrammarDetailsActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var topicNameTextView: TextView
    private lateinit var grammarSubtopicRepository: GrammarSubtopicRepository
    private lateinit var grammarTopicRepository: GrammarTopicRepository
    private val uiScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grammar_details)

        val firestore = FirebaseFirestore.getInstance()
        val firestoreGrammarTopicRepository = FirestoreGrammarTopicRepository(firestore)
        val firestoreGrammarSubtopicRepository = FirestoreGrammarSubtopicRepository(firestore)

        grammarTopicRepository = GrammarTopicRepository(firestoreGrammarTopicRepository)
        grammarSubtopicRepository = GrammarSubtopicRepository(firestoreGrammarSubtopicRepository)

        recyclerView = findViewById(R.id.recyclerView)
        topicNameTextView = findViewById(R.id.textTopicName)

        val backButton: ImageButton = findViewById(R.id.btnBack)
        backButton.setOnClickListener { finish() }

        // Get topicId from intent
        val grammarTopicId = intent.getStringExtra("topic_id")

        Log.d("grammarTopicId", grammarTopicId.toString())
        if (!grammarTopicId.isNullOrEmpty()) {  // Check if the topic ID is not null or empty
            uiScope.launch {
                val subtopics = getSubtopicsForTopic(grammarTopicId)  // Pass String as topicId
                val topicName = getTopicNameById(grammarTopicId)  // Pass String as topicId
                topicNameTextView.text = topicName  // Display the topic name
                setupRecyclerView(subtopics)
            }
        } else {
            // Handle case when topicId is invalid
            topicNameTextView.text = "Invalid Topic ID"
        }

        val btnPractice:Button = findViewById(R.id.btnPractice)
        btnPractice.setOnClickListener{
            // đi đến trang luyện tập ngữ pháp theo topic này với random các câu trắc nghiệm
            val intent = android.content.Intent(this, GrammarTopicPracticeActivity::class.java)
            intent.putExtra("grammarTopicId", grammarTopicId)
            startActivity(intent)
        }
    }

    // Function to fetch all subtopics from the repository
    private suspend fun getSubtopicsForTopic(topicId: String): List<GrammarSubtopic> {
        return withContext(Dispatchers.IO) {
            // Gọi repository để lấy danh sách subtopics
            val allSubtopics = grammarSubtopicRepository.getSubtopicsByTopicId(topicId)

            // Log danh sách subtopics
            Log.d("allSubtopics", allSubtopics.toString())

            // Trả về kết quả
            allSubtopics
        }
    }


    // Function to fetch the topic name by topicId
    private suspend fun getTopicNameById(topicId: String): String {
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





