package com.android_ai.csc13009.app.presentation.activity

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.local.AppDatabase
import com.android_ai.csc13009.app.data.repository.WordScheduleRepository
import com.android_ai.csc13009.app.domain.models.WordModel
import com.android_ai.csc13009.app.domain.models.WordSchedule
import com.android_ai.csc13009.app.presentation.fragment.AddWordDialogFragment
import com.android_ai.csc13009.app.presentation.viewmodel.WordScheduleViewModel
import com.android_ai.csc13009.app.utils.adapter.WordScheduleAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.content.Context
import android.content.Intent
import android.widget.Button
import android.widget.TextView

class WordScheduleActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var btnAddWord: FloatingActionButton
    private lateinit var btnReview: ImageView
    private lateinit var wordScheduleAdapter: WordScheduleAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: WordScheduleViewModel
    private lateinit var wordScheduleRepository: WordScheduleRepository

    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_word_schedule)

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

        btnBack = findViewById(R.id.ivBack)
        btnBack.setOnClickListener {
            onBackPressed()
        }

        btnAddWord = findViewById(R.id.fabAddWord)
        btnAddWord.setOnClickListener{
            showAddWordDialog()
        }

        btnReview = findViewById(R.id.ivReview)
        btnReview.setOnClickListener {
            lifecycleScope.launch {
                try {
                    val currentTime = System.currentTimeMillis()
                    Log.d("Review", "Current Time: $currentTime")
                    val dueWords: List<WordSchedule> = viewModel.getWordsForReview(userId, currentTime)
                    if (dueWords.isNotEmpty()) {
                        val intent = Intent(this@WordScheduleActivity, ReviewWordsActivity::class.java)
                        intent.putParcelableArrayListExtra("dueWords", ArrayList(dueWords))
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@WordScheduleActivity, "No words to review!", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@WordScheduleActivity, "Error loading words: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        setupRecyclerView()
        fetchWordSchedules()
    }

    private fun setupViewModel() {
        val database = AppDatabase.getInstance(this)
        val dao = database.wordScheduleDao()
        wordScheduleRepository = WordScheduleRepository(dao)
        viewModel = WordScheduleViewModel(wordScheduleRepository)
    }

    private fun setupRecyclerView() {
        wordScheduleAdapter = WordScheduleAdapter(this, emptyList()) { wordSchedule ->
            showDeleteConfirmationDialog(wordSchedule)
        }
        recyclerView = findViewById(R.id.rvScheduledWords)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@WordScheduleActivity)
            adapter = wordScheduleAdapter
        }
    }

    private fun showAddWordDialog() {
        val addWordDialog = AddWordDialogFragment()
        addWordDialog.setOnWordAddedListener { newWordSchedule ->
            addWord(newWordSchedule)
        }
        addWordDialog.show(supportFragmentManager, "AddWordDialog")
    }

    private fun addWord(wordSchedule: WordSchedule) {
        lifecycleScope.launch {
            try {
                viewModel.insertOrUpdateWordSchedule(userId, wordSchedule)
                fetchWordSchedules()
                Toast.makeText(this@WordScheduleActivity, "Word added successfully.", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this@WordScheduleActivity, "Failed to add word: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchWordSchedules() {
        lifecycleScope.launch {
            try {
                val wordSchedules = viewModel.getAllWordSchedules(userId)
                Log.d("DatabaseCheck", "Fetched schedules: $wordSchedules")
                wordScheduleAdapter.updateList(wordSchedules)
            } catch (e: Exception) {
                Toast.makeText(this@WordScheduleActivity, "Failed to fetch words: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDeleteConfirmationDialog(wordSchedule: WordSchedule) {
        // Inflate the custom layout
        val dialogView = layoutInflater.inflate(R.layout.dialog_delete_confirmation, null)

        // Access views in the custom layout
        val ivWarning = dialogView.findViewById<ImageView>(R.id.ivWarning)
        val tvDeleteTitle = dialogView.findViewById<TextView>(R.id.tvDeleteTitle)
        val tvDeleteMessage = dialogView.findViewById<TextView>(R.id.tvDeleteMessage)
        val btnConfirmDelete = dialogView.findViewById<Button>(R.id.btnConfirmDelete)
        val btnCancelDelete = dialogView.findViewById<Button>(R.id.btnCancelDelete)

        // Update the message dynamically with the word ID
        tvDeleteMessage.text = "Are you sure you want to delete this schedule?"

        // Create the dialog
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        // Confirm Delete button action
        btnConfirmDelete.setOnClickListener {
            deleteWord(wordSchedule)
            dialog.dismiss()
        }

        // Cancel button action
        btnCancelDelete.setOnClickListener {
            dialog.dismiss()
        }

        // Show the dialog
        dialog.show()
    }


    private fun deleteWord(wordSchedule: WordSchedule) {
        lifecycleScope.launch {
            try {
                viewModel.deleteWordSchedule(userId, wordSchedule.wordId)
                fetchWordSchedules()
                Toast.makeText(this@WordScheduleActivity, "Word successfully removed.", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this@WordScheduleActivity, "Failed to remove word: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        //Log.d("WordScheduleActivity", "onResume called")
        fetchWordSchedules() // Refresh the data when returning to this activity
    }
}