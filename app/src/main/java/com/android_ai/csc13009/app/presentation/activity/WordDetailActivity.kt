package com.android_ai.csc13009.app.presentation.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.remote.model.TagState
import com.android_ai.csc13009.app.data.remote.repository.FirestoreTagRepository
import com.android_ai.csc13009.app.data.repository.TagRepository
import com.android_ai.csc13009.app.domain.models.Tag
import com.android_ai.csc13009.app.domain.models.WordDetailItem
import com.android_ai.csc13009.app.presentation.viewmodel.TagViewModel
import com.android_ai.csc13009.app.utils.adapter.WordDetailAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class WordDetailActivity : AppCompatActivity() {
    private lateinit var tvWordHeader: TextView
    private lateinit var tvWordName: TextView
    private lateinit var tvPronunciation: TextView
    private lateinit var backButton: ImageView
    private lateinit var bookmarkButton: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var wordDetailAdapter: WordDetailAdapter

    private lateinit var tagViewModel: TagViewModel
    private var wordId: Int = -1 // Current word's ID
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_word_detail)

        // Ánh xạ các thành phần giao diện
        tvWordHeader = findViewById(R.id.tvWordHeader)
        tvWordName = findViewById(R.id.tvWord)
        tvPronunciation = findViewById(R.id.tvPronunciation)
        backButton = findViewById(R.id.ivBack)
        bookmarkButton = findViewById(R.id.ivBookmark)
        recyclerView = findViewById(R.id.rvWordDetails)

        // Thiết lập hành động nút quay lại
        backButton.setOnClickListener { finish() }

        // Lấy dữ liệu từ Intent
        wordId = intent.getIntExtra("word_id", -1)
        val wordText = intent.getStringExtra("word_text") ?: ""
        val wordPronunciation = intent.getStringExtra("word_pronunciation") ?: ""
        val wordDetails = intent.getStringExtra("word_details") ?: ""

        // Initialize ViewModel
        val tagRepository = TagRepository(FirestoreTagRepository(FirebaseFirestore.getInstance()))
        tagViewModel = TagViewModel(tagRepository)
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            // Handle user not logged in case
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show()
            return
        }
        userId = currentUser.uid

        // Hiển thị dữ liệu
        tvWordHeader.text = wordText
        tvWordName.text = wordText
        tvPronunciation.text = wordPronunciation

        // Phân tích và hiển thị danh sách chi tiết từ
        val wordDetailItems: List<WordDetailItem> = parseRawData(wordDetails)
        wordDetailAdapter = WordDetailAdapter(wordDetailItems)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = wordDetailAdapter

        // Quan sát thay đổi từ ViewModel
        observeTags()

        // Xử lý khi nhấn nút Bookmark
        bookmarkButton.setOnClickListener {
            if (wordId != -1) {
                tagViewModel.getUserTags(userId) // Lấy danh sách tag
            } else {
                Toast.makeText(this, "Invalid word ID", Toast.LENGTH_SHORT).show()
            }
        }
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

    private fun observeTags() {
        tagViewModel.tagState.observe(this) { state ->
            when (state) {
                is TagState.TagList -> {
                    if (state.tags.isNotEmpty()) {
                        showTagSelectionDialog(state.tags)
                    } else {
                        Toast.makeText(this, "No tags available", Toast.LENGTH_SHORT).show()
                    }
                }
                is TagState.Success -> {
                    Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                }
                is TagState.Error -> {
                    Toast.makeText(this, state.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showTagSelectionDialog(tags: List<Tag>) {
        val tagNames = tags.map { it.name }.toTypedArray()

        AlertDialog.Builder(this)
            .setTitle("Select a Tag")
            .setItems(tagNames) { dialog, which ->
                val selectedTag = tags[which]
                addWordToTag(selectedTag.id, wordId)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun addWordToTag(tagId: String, wordId: Int) {
        tagViewModel.addWordToTag(tagId, wordId)
    }
}
