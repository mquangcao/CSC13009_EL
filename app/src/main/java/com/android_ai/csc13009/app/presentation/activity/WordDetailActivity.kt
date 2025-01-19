package com.android_ai.csc13009.app.presentation.activity

import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
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
import com.android_ai.csc13009.app.data.repository.WordPhoneticRepository
import com.android_ai.csc13009.app.domain.models.Tag
import com.android_ai.csc13009.app.domain.models.WordDetailItem
import com.android_ai.csc13009.app.domain.repository.IWordPhoneticRepository
import com.android_ai.csc13009.app.presentation.service.DictionaryApiService
import com.android_ai.csc13009.app.presentation.viewmodel.TagViewModel
import com.android_ai.csc13009.app.utils.adapter.WordDetailAdapter
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.Retrofit

import retrofit2.converter.gson.GsonConverterFactory

class WordDetailActivity : AppCompatActivity() {
    private lateinit var tvWordHeader: TextView
    private lateinit var tvWordName: TextView
    private lateinit var tvPronunciation: TextView
    private lateinit var backButton: ImageView
    private lateinit var bookmarkButton: ImageView
    private lateinit var soundButton: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var wordDetailAdapter: WordDetailAdapter

    private lateinit var tagViewModel: TagViewModel
    private var wordId: Int = -1 // Current word's ID
    private lateinit var userId: String

    private var exoPlayer: ExoPlayer? = null

    private lateinit var repository: IWordPhoneticRepository

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
        soundButton = findViewById(R.id.ivSound)
        recyclerView = findViewById(R.id.rvWordDetails)

        // Thiết lập hành động nút quay lại
        backButton.setOnClickListener {
            exoPlayer?.release()
            finish()
        }

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

        // Initialize repository
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.dictionaryapi.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService = retrofit.create(DictionaryApiService::class.java)
        repository = WordPhoneticRepository(apiService)

        // Handle button click
        soundButton.setOnClickListener {
            fetchAndPlayPronunciation(wordText)
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
        // Inflate the custom layout
        val dialogView = layoutInflater.inflate(R.layout.dialog_tag_selection, null)

        // Access views in the custom layout
        val lvTags = dialogView.findViewById<ListView>(R.id.lvTags)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)

        // Set up the ListView with tag names
        val tagNames = tags.map { it.name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, tagNames)
        lvTags.adapter = adapter

        // Create the dialog
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        // Handle tag selection
        lvTags.setOnItemClickListener { _, _, position, _ ->
            val selectedTag = tags[position]
            addWordToTag(selectedTag.id, wordId)
            dialog.dismiss()
        }

        // Cancel button action
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        // Show the dialog
        dialog.show()
    }


    private fun addWordToTag(tagId: String, wordId: Int) {
        tagViewModel.addWordToTag(tagId, wordId)
    }

    private fun fetchAndPlayPronunciation(word: String) {
        repository.getWordPhonetics(
            word,
            onSuccess = { phonetics ->
                val audioUrl = phonetics.firstOrNull()?.audio
                if (!audioUrl.isNullOrEmpty()) {
                    playAudio(audioUrl)
                } else {
                    showToast("Pronunciation not available")
                }
            },
            onError = { error ->
                showToast(error)
            }
        )
    }

    private fun playAudio(audioUrl: String) {
        exoPlayer?.release() // Release any previous ExoPlayer instance
        exoPlayer = ExoPlayer.Builder(this).build().apply {
            val mediaItem = MediaItem.fromUri(Uri.parse(audioUrl))
            setMediaItem(mediaItem)
            prepare()
            play()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer?.release()
    }
}
