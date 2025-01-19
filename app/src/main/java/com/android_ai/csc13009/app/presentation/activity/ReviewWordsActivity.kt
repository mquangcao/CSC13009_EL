package com.android_ai.csc13009.app.presentation.activity

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.local.AppDatabase
import com.android_ai.csc13009.app.data.repository.WordRepository
import com.android_ai.csc13009.app.data.repository.WordScheduleRepository
import com.android_ai.csc13009.app.domain.models.WordDetailItem
import com.android_ai.csc13009.app.domain.models.WordSchedule
import com.android_ai.csc13009.app.presentation.service.CalculateNextReviewDateUseCase
import com.android_ai.csc13009.app.presentation.viewmodel.ReviewWordsViewModel
import com.android_ai.csc13009.app.utils.adapter.WordDetailAdapter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReviewWordsActivity : AppCompatActivity() {
    private lateinit var wordScheduleRepository: WordScheduleRepository
    private lateinit var wordRepository: WordRepository
    private lateinit var viewModel: ReviewWordsViewModel
    private lateinit var words: List<WordSchedule>
    private var currentWordIndex = 0
    private lateinit var progressBar: ProgressBar
    private lateinit var progressText: TextView
    private lateinit var wordTextView: TextView
    private lateinit var revealCard: CardView

    private lateinit var userId: String

    private lateinit var completionPopup: View
    private lateinit var returnButton: Button
    private lateinit var revealButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var wordDetailAdapter: WordDetailAdapter

    private var isRevealed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_words)

        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            // Handle user not logged in case
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        userId = currentUser.uid

        setupViewModel()

        words = intent.getParcelableArrayListExtra<WordSchedule>("dueWords") ?: emptyList()
        if (words.isEmpty()) {
            Toast.makeText(this, "No words available for review.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        progressBar = findViewById(R.id.progressBar)
        progressText = findViewById(R.id.progressText)
        wordTextView = findViewById(R.id.wordTextView)

        progressBar.max = words.size

        revealCard = findViewById(R.id.revealCard)
        recyclerView = findViewById(R.id.definitionRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        wordDetailAdapter = WordDetailAdapter(emptyList())
        recyclerView.adapter = wordDetailAdapter
        revealCard.visibility = View.GONE // Initially hidden

        findViewById<Button>(R.id.correctButton).setOnClickListener {
            updateWord(true)
        }

        findViewById<Button>(R.id.incorrectButton).setOnClickListener {
            updateWord(false)
        }

        // Initialize the popup
        completionPopup = findViewById(R.id.completionPopup)

        revealButton = findViewById(R.id.revealButton)
        revealButton.setOnClickListener {
            isRevealed = !isRevealed
            if (isRevealed) {
                revealButton.text = "Hide"
                revealButton.setBackgroundColor(ContextCompat.getColor(this, R.color.gray))
            } else {
                revealButton.text = "Reveal"
                revealButton.setBackgroundColor(ContextCompat.getColor(this, R.color.black))
            }
            toggleRecyclerViewVisibility()
        }

        returnButton = findViewById(R.id.returnButton)
        returnButton.setOnClickListener {
            finish() // Close activity on return
        }

        displayCurrentWord()
    }

    private fun setupViewModel() {
        val database = AppDatabase.getInstance(this)
        val dao = database.wordScheduleDao()
        val wordDao = database.wordDao()
        wordRepository = WordRepository(wordDao)
        wordScheduleRepository = WordScheduleRepository(dao)
        viewModel = ReviewWordsViewModel(wordScheduleRepository, CalculateNextReviewDateUseCase(), userId)
    }

    private fun displayCurrentWord() {
        GlobalScope.launch(Dispatchers.Main) {
            if (currentWordIndex < words.size) {
                val wordText = withContext(Dispatchers.IO) {
                    wordRepository.getWordById(words[currentWordIndex].wordId)?.word
                }
                val wordDetails = withContext(Dispatchers.IO) {
                    wordRepository.getWordById(words[currentWordIndex].wordId)?.details
                }

                val wordDetailItems: List<WordDetailItem> = parseRawData(wordDetails)
                wordDetailAdapter.updateData(wordDetailItems)

                wordTextView.text = "Word: ${wordText}"
                progressBar.progress = currentWordIndex
                progressText.text = "${currentWordIndex}/${words.size}"
            } else {
                wordTextView.text = "All words reviewed!"
                progressBar.progress = words.size
                progressText.text = "${currentWordIndex}/${words.size}"
                revealButton.visibility = View.INVISIBLE
                showCompletionPopup() // Show popup animation when all words are reviewed
            }
        }
    }

    private fun updateWord(isCorrect: Boolean) {
        if (currentWordIndex < words.size) {
            val word = words[currentWordIndex]
            viewModel.updateWordReview(word, isCorrect)
            currentWordIndex++
            //progressBar.progress = currentWordIndex
            //progressText.text = "${currentWordIndex}/${words.size}"
            displayCurrentWord()
        }
    }

    private fun toggleRecyclerViewVisibility() {
        if (revealCard.visibility == View.GONE) {
            revealCard.visibility = View.VISIBLE
        } else {
            revealCard.visibility = View.GONE
        }
    }

    private fun showCompletionPopup() {
        completionPopup.visibility = View.VISIBLE
        completionPopup.alpha = 0f

        findViewById<Button>(R.id.correctButton).visibility = View.INVISIBLE
        findViewById<Button>(R.id.incorrectButton).visibility = View.INVISIBLE

        // Animate the popup
        completionPopup.animate()
            .alpha(1f)
            .setDuration(500)
            .start()
    }

    private fun parseRawData(rawData: String?): List<WordDetailItem> {
        val items = mutableListOf<WordDetailItem>()
        // Split the input string by '|'
        val parts = rawData?.split("|")

        // Iterate through each part and determine its type
        if (parts != null) {
            for (part in parts) {
                if (part != "") {
                    val trimmedPart = part.trim()

                    when {
                        trimmedPart.startsWith("*") -> {
                            items.add(WordDetailItem("*", trimmedPart.substring(2).trim()))
                        }

                        trimmedPart.startsWith("-") -> {
                            items.add(WordDetailItem("-", trimmedPart.substring(2).trim()))
                        }

                        trimmedPart.startsWith("=") -> {
                            items.add(WordDetailItem("=", trimmedPart.substring(1).trim()))
                        }

                        trimmedPart.startsWith("+") -> {
                            items.add(WordDetailItem("+", trimmedPart.substring(2).trim()))
                        }

                        else -> {
                            items.add(WordDetailItem("*", trimmedPart.trim()))
                        }
                    }
                }
            }
        }
        return items
    }
}
