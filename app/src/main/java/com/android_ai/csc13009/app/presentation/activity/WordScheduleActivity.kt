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

class WordScheduleActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var btnAddWord: FloatingActionButton
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

        btnBack = findViewById(R.id.ivBack)
        btnBack.setOnClickListener {
            onBackPressed()
        }

        btnAddWord = findViewById(R.id.fabAddWord)
        btnAddWord.setOnClickListener{
            showAddWordDialog()
        }

        setupViewModel()
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
        AlertDialog.Builder(this)
            .setTitle("Delete Word")
            .setMessage("Are you sure you want to delete '${wordSchedule.wordId}'?")
            .setPositiveButton("Yes") { _, _ ->
                deleteWord(wordSchedule)
            }
            .setNegativeButton("No", null)
            .show()
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
}