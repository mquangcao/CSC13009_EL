package com.android_ai.csc13009.app.presentation.activity

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
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
import com.android_ai.csc13009.app.domain.models.GrammarQuestion
import com.android_ai.csc13009.app.domain.models.Question
import com.android_ai.csc13009.app.utils.adapter.MistakeAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class MistakesActivity : AppCompatActivity() {
    private lateinit var rvMistakes: RecyclerView
    private lateinit var mistakeAdapter: MistakeAdapter
    private val repository = FirestoreLearningDetailRepository(FirebaseFirestore.getInstance())
    private val grammarQuestionRepository = GrammarQuestionRepository(FirestoreGrammarQuestionRepository(FirebaseFirestore.getInstance()))
    private val firestoreQuestionRepository = FirestoreQuestionRepository(FirebaseFirestore.getInstance())

    private lateinit var btnBack: ImageView

    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mistakes)

        rvMistakes = findViewById(R.id.rvMistakes)
        btnBack = findViewById(R.id.backIcon)
        rvMistakes.layoutManager = LinearLayoutManager(this)

        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            // Handle user not logged in case
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show()
            return
        }
        userId = currentUser.uid

        lifecycleScope.launch {
            val mistakes = repository.getLearningDetailByUsers(userId)
            Log.d("DatabaseCheck", "Fetched details: $mistakes")

            val grammarQuestionList = mutableListOf<GrammarQuestion>()
            val vocabQuestionList = mutableListOf<FirestoreQuestion>()

            for (mistake in mistakes) {
                when (mistake.type.lowercase()) {
                    "grammar" -> {
                        val grammarQuestion = grammarQuestionRepository.getQuestionById(mistake.questionId)
                        if (grammarQuestion != null) {
                            grammarQuestionList.add(grammarQuestion)
                        }
                    }
                    "vocabulary" -> {
                        val vocabQuestion = firestoreQuestionRepository.getQuestionById(mistake.questionId)
                        //val vocabQuestion = doc.toObject(Question::class.java)
                        if (vocabQuestion != null) {
                            vocabQuestionList.add(vocabQuestion)
                        }
                    }
                }
            }

            mistakeAdapter = MistakeAdapter(
                grammarQuestions = grammarQuestionList,
                vocabQuestions = vocabQuestionList,
                //listeningQuestions = listeningQuestionList
            )

            rvMistakes.adapter = mistakeAdapter
        }

        btnBack.setOnClickListener{
            finish()
        }
    }
}