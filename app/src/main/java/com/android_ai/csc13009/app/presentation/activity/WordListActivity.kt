package com.android_ai.csc13009.app.presentation.activity

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.local.AppDatabase
import com.android_ai.csc13009.app.data.remote.repository.FirestoreTagRepository
import com.android_ai.csc13009.app.data.repository.TagRepository
import com.android_ai.csc13009.app.data.repository.WordRepository
import com.android_ai.csc13009.app.domain.models.WordModel
import com.android_ai.csc13009.app.utils.adapter.WordListAdapter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WordListActivity : AppCompatActivity() {
    private lateinit var wordListAdapter: WordListAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var tagRepository: TagRepository
    private lateinit var wordRepository: WordRepository
    private lateinit var btnBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_word_list)

        btnBack = findViewById(R.id.ivBack)
        btnBack.setOnClickListener {
            onBackPressed()
        }

        // Initialize Room Database and DAO
        val database = AppDatabase.getInstance(this)
        val wordDao = database.wordDao()
        wordRepository = WordRepository(wordDao)
        tagRepository = TagRepository(FirestoreTagRepository(FirebaseFirestore.getInstance()))


        setupRecyclerView()

        // Retrieve tagId from intent
        val tagId = intent.getStringExtra("TAG_ID")
        if (tagId != null) {
            fetchTag(tagId)
        } else {
            Toast.makeText(this, "Tag ID not found", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setupRecyclerView() {
        wordListAdapter = WordListAdapter(emptyList()) { wordModel ->
            showDeleteConfirmationDialog(wordModel)
        }
        recyclerView = findViewById(R.id.rvTagWordList)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@WordListActivity)
            adapter = wordListAdapter
        }
    }

    private fun fetchTag(tagId: String) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                // Fetch the Tag on the IO thread
                val wordIds = withContext(Dispatchers.IO) {
                    val tag = tagRepository.getTagById(tagId)
                    Log.d("WordListActivity", "Fetched tag: $tag")
                    tag?.wordIds ?: emptyList()
                }

                // Fetch Word models based on wordIds
                val words = withContext(Dispatchers.IO) {
                    wordIds.mapNotNull { wordId ->
                        wordRepository.getWordById(wordId) // Replace with your WordRepository fetching logic
                    }
                }

                // Update the adapter with the fetched words
                wordListAdapter.updateList(words)
            } catch (e: Exception) {
                Toast.makeText(this@WordListActivity, "Error fetching words: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDeleteConfirmationDialog(wordModel: WordModel) {
        AlertDialog.Builder(this)
            .setTitle("Delete Word")
            .setMessage("Are you sure you want to delete '${wordModel.word}'?")
            .setPositiveButton("Yes") { _, _ ->
                deleteWord(wordModel)
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun deleteWord(wordModel: WordModel) {
        // Implement word deletion logic here
    }
}