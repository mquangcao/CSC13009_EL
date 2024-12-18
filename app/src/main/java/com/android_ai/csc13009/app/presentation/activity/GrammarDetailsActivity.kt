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
import com.android_ai.csc13009.app.domain.repository.model.GrammarTopic
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

        val backButton: ImageButton = findViewById(R.id.btnBack)
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

//    // Function to fetch all subtopics from the repository
//    private suspend fun getSubtopicsForTopic(topicId: Int): List<GrammarSubtopic> {
//        return withContext(Dispatchers.IO) {
//            // You can use the getAllSubtopics method here
//            val allSubtopics = grammarSubtopicRepository.getAllSubtopics() // Get all subtopics
//            // Filter subtopics by topicId
//            allSubtopics.filter { it.topicId == topicId }
//        }
//    }
//
//    // Function to fetch the topic name by topicId
//    private suspend fun getTopicNameById(topicId: Int): String {
//        return withContext(Dispatchers.IO) {
//            // Use getAllTopics to fetch all topics and filter by topicId
//            val allTopics = grammarTopicRepository.getAllTopics() // Get all topics
//            val topic = allTopics.find { it.id == topicId }
//            topic?.name ?: "Unknown Topic"  // Return the topic name, or a default if not found
//        }
//    }

    private suspend fun getSubtopicsForTopic(topicId: Int): List<GrammarSubtopic> {
        // Mock data cho Grammar Subtopics
        val mockGrammarSubtopics = listOf(
            GrammarSubtopic(
                id = 1,
                topicId = 1,
                name = "Simple Present",
                content = "Describes habits, routines, and general facts.",
                structures = "Subject + Verb (base form) + Object",
                examples = "He works every day. She plays the piano."
            ),
            GrammarSubtopic(
                id = 2,
                topicId = 1,
                name = "Present Continuous",
                content = "Describes actions happening now.",
                structures = "Subject + is/are + Verb(ing) + Object",
                examples = "She is reading a book. They are playing football."
            ),
            GrammarSubtopic(
                id = 3,
                topicId = 2,
                name = "Simple Past",
                content = "Describes actions that happened in the past.",
                structures = "Subject + Verb (past form) + Object",
                examples = "He went to the market. She watched a movie."
            ),
            GrammarSubtopic(
                id = 4,
                topicId = 2,
                name = "Past Continuous",
                content = "Describes actions happening in the past at a specific time.",
                structures = "Subject + was/were + Verb(ing) + Object",
                examples = "She was reading a book when I called her. They were playing football all afternoon."
            ),
            GrammarSubtopic(
                id = 5,
                topicId = 3,
                name = "Future Simple",
                content = "Describes actions that will happen in the future.",
                structures = "Subject + will + Verb (base form) + Object",
                examples = "She will travel tomorrow. We will meet next week."
            ),
            GrammarSubtopic(
                id = 6,
                topicId = 4,
                name = "Nouns",
                content = "Words that refer to people, places, things, or ideas.",
                structures = "Nouns can be singular or plural, countable or uncountable.",
                examples = "apple (singular), apples (plural), water (uncountable)"
            ),
            GrammarSubtopic(
                id = 7,
                topicId = 5,
                name = "Sentence Structures",
                content = "Different ways of structuring sentences in English.",
                structures = "Subject + Verb + Object, Subject + Verb + Complement, etc.",
                examples = "She is a teacher. I am eating lunch."
            ),
            GrammarSubtopic(
                id = 8,
                topicId = 6,
                name = "Subject-Verb Agreement",
                content = "Ensures the subject and verb agree in number.",
                structures = "Singular subjects take singular verbs, plural subjects take plural verbs.",
                examples = "She runs fast. They run fast."
            ),
            GrammarSubtopic(
                id = 9,
                topicId = 7,
                name = "First Conditional",
                content = "Used for real possibilities in the future.",
                structures = "If + Present Simple, + will + base verb",
                examples = "If it rains, I will stay at home."
            ),
            GrammarSubtopic(
                id = 10,
                topicId = 8,
                name = "Modals of Permission",
                content = "Modals used to express permission.",
                structures = "Can, Could, May, Might",
                examples = "Can I go to the bathroom? May I help you?"
            )
        )


        return withContext(Dispatchers.IO) {
            mockGrammarSubtopics.filter { it.topicId == topicId }
        }
    }

    private suspend fun getTopicNameById(topicId: Int): String {
        // Mock data cho Grammar Topics
        val mockGrammarTopics = listOf(
            GrammarTopic(id = 1, levelId = 1, name = "Present Tense"),
            GrammarTopic(id = 2, levelId = 1, name = "Past Tense"),
            GrammarTopic(id = 3, levelId = 2, name = "Future Tense"),
            GrammarTopic(id = 4, levelId = 2, name = "Parts of Speech"),
            GrammarTopic(id = 5, levelId = 3, name = "Sentence Structures"),
            GrammarTopic(id = 6, levelId = 1, name = "Subject-Verb Agreement"),
            GrammarTopic(id = 7, levelId = 3, name = "Conditionals"),
            GrammarTopic(id = 8, levelId = 2, name = "Modals"),
            GrammarTopic(id = 9, levelId = 3, name = "Reported Speech"),
            GrammarTopic(id = 10, levelId = 3, name = "Phrasal Verbs")
        )

        return withContext(Dispatchers.IO) {
            mockGrammarTopics.find { it.id == topicId }?.name ?: "Unknown Topic"
        }
    }



    // Function to setup the RecyclerView with the list of subtopics
    private fun setupRecyclerView(subtopics: List<GrammarSubtopic>) {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = GrammarSubtopicAdapter(subtopics)
    }
}





