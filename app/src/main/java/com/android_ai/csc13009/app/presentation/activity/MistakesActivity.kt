package com.android_ai.csc13009.app.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.remote.model.FirestoreQuestion
import com.android_ai.csc13009.app.data.remote.repository.FirestoreGrammarQuestionRepository
import com.android_ai.csc13009.app.data.remote.repository.FirestoreLearningDetailRepository
import com.android_ai.csc13009.app.data.remote.repository.FirestoreQuestionRepository
import com.android_ai.csc13009.app.data.repository.GrammarQuestionRepository
import com.android_ai.csc13009.app.data.repository.ListeningQuestionRepository
import com.android_ai.csc13009.app.data.repository.QuestionRepository
import com.android_ai.csc13009.app.domain.models.GrammarQuestion
import com.android_ai.csc13009.app.domain.models.ListeningQuestion
import com.android_ai.csc13009.app.domain.models.Question
import com.android_ai.csc13009.app.utils.adapter.MistakeAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class MistakesActivity : AppCompatActivity() {
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var rvMistakes: RecyclerView
    private lateinit var mistakeAdapter: MistakeAdapter
    private val repository = FirestoreLearningDetailRepository(FirebaseFirestore.getInstance())
    private val grammarQuestionRepository = GrammarQuestionRepository(FirestoreGrammarQuestionRepository(FirebaseFirestore.getInstance()))
    private val questionRepository = QuestionRepository(this)
    private val listeningRepository = ListeningQuestionRepository(this)
    private lateinit var btnBack: ImageView
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mistakes)

        // Initialize views
        rvMistakes = findViewById(R.id.rvMistakes)
        btnBack = findViewById(R.id.backIcon)
        rvMistakes.layoutManager = LinearLayoutManager(this)

        // Initialize result launcher
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                Log.d("MistakesActivity", "ActivityResult: OK result received.")
                refreshData() // Refresh the list when RESULT_OK is received.
            } else {
                Log.d("MistakesActivity", "ActivityResult: Non-OK result received.")
            }
        }

        // Initialize adapter with empty lists
        mistakeAdapter = MistakeAdapter(emptyList(), emptyList(), emptyList(), activityResultLauncher)
        rvMistakes.adapter = mistakeAdapter

        // Initialize back button
        btnBack.setOnClickListener { finish() }

        // Fetch user details and refresh data
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            userId = currentUser.uid
            refreshData()
        } else {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun refreshData() {
        lifecycleScope.launch {
            val mistakes = repository.getLearningDetailByUsers(userId)

            val grammarQuestionList = mutableListOf<GrammarQuestion>()
            val vocabQuestionList = mutableListOf<Question>()
            val listeningQuestionList = mutableListOf<ListeningQuestion>()

            // Process mistakes into categorized lists
            for (mistake in mistakes) {
                when (mistake.type.lowercase()) {
                    "grammar" -> grammarQuestionRepository.getQuestionById(mistake.questionId)?.let { grammarQuestionList.add(it) }
                    "vocabulary" -> questionRepository.getQuestion(mistake.questionId)?.let { vocabQuestionList.add(it) }
                    "listening" -> listeningRepository.getQuestionById(mistake.questionId)?.let { listeningQuestionList.add(it) }
                }
            }

            // Update adapter data
            mistakeAdapter.updateData(grammarQuestionList, vocabQuestionList, listeningQuestionList)
        }
    }
}
