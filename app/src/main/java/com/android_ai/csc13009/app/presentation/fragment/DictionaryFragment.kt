package com.android_ai.csc13009.app.presentation.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.local.AppDatabase
import com.android_ai.csc13009.app.data.repository.WordRepository
import com.android_ai.csc13009.app.domain.models.WordModel
import com.android_ai.csc13009.app.presentation.activity.DashboardActivity
import com.android_ai.csc13009.app.presentation.activity.TagActivity
import com.android_ai.csc13009.app.presentation.activity.WordDetailActivity
import com.android_ai.csc13009.app.presentation.activity.WordScheduleActivity
import com.android_ai.csc13009.app.utils.adapter.DictionaryAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DictionaryFragment : Fragment() {
    private lateinit var etSearch: AutoCompleteTextView
    private lateinit var rvSearchResults: RecyclerView
    private lateinit var btnTag: ImageView
    private lateinit var wordScheduleBanner: RelativeLayout
    private lateinit var dictionaryAdapter: DictionaryAdapter

    private lateinit var userId: String

    private val wordModelList = mutableListOf<WordModel>()
    private lateinit var wordRepository: WordRepository
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_dictionary, container, false)
        // Initialize AutoCompleteTextView
        etSearch = view.findViewById(R.id.etSearch)
        rvSearchResults = view.findViewById(R.id.rvSearchResults)
        btnTag = view.findViewById(R.id.ivTagButton)
        wordScheduleBanner = view.findViewById(R.id.rlWordSchedule)

        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser

        if (currentUser != null) {
            userId = currentUser.uid
        }

        rvSearchResults.layoutManager = LinearLayoutManager(requireContext())
        dictionaryAdapter = DictionaryAdapter(emptyList()) { wordModel ->
            saveSearchedWord(userId, wordModel.id)
            val context = requireContext()
            val intent = Intent(context, WordDetailActivity::class.java)
            intent.putExtra("word_id", wordModel.id)
            intent.putExtra("word_text", wordModel.word) // Truyền từ cần hiển thị
            intent.putExtra("word_pronunciation", wordModel.pronunciation)
            intent.putExtra("word_details", wordModel.details)
            context.startActivity(intent)
        }// Initial empty list
        rvSearchResults.adapter = dictionaryAdapter // Attach adapter immediately
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Apply window insets
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Room Database and DAO
        val database = AppDatabase.getInstance(requireContext())
        val wordDao = database.wordDao()
        wordRepository = WordRepository(wordDao)


        // Listen for text changes in the search bar
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    // When search bar is empty, display words from the searched list
                    rvSearchResults.visibility = View.VISIBLE
                    updateDictionaryWithSearchedWords() // Update adapter with searched words
                } else {
                    rvSearchResults.visibility = View.VISIBLE
                    searchWords(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Listen for when the search bar is clicked (focused)
        etSearch.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                // If the search bar is focused, display words from the searched list
                rvSearchResults.visibility = View.VISIBLE
                updateDictionaryWithSearchedWords() // Update adapter with searched words
            } else {
                rvSearchResults.visibility = View.GONE
            }
        }

        btnTag.setOnClickListener{
            startActivity(Intent(requireContext(), TagActivity::class.java))
        }

        wordScheduleBanner.setOnClickListener {
            val intent = Intent(context, WordScheduleActivity::class.java)
            startActivity(intent)
        }
    }

    // Function to search words as user types
    private fun searchWords(query: String) {
        GlobalScope.launch(Dispatchers.Main) {
            val words = withContext(Dispatchers.IO) {
                wordRepository.getSuggestions(query)
            }

            dictionaryAdapter.updateList(words)
        }
    }

    fun getUserSharedPreferences(userId: String): SharedPreferences {
        return requireContext().getSharedPreferences("searched_words_$userId", Context.MODE_PRIVATE)
    }

    fun saveSearchedWord(userId: String, wordId: Int) {
        val sharedPreferences = getUserSharedPreferences(userId)
        val wordList = getSearchedWords(userId).toMutableList()

        wordList.remove(wordId)
        wordList.add(0, wordId)
        if (wordList.size > 20) {
            wordList.removeAt(wordList.size - 1)
        }

        sharedPreferences.edit().putString("words", Gson().toJson(wordList)).apply()
    }

    fun getSearchedWords(userId: String): List<Int> {
        val sharedPreferences = getUserSharedPreferences(userId)
        val json = sharedPreferences.getString("words", "[]") ?: "[]"

        // Safe JSON parsing
        return try {
            Gson().fromJson(json, List::class.java) as List<Int>
        } catch (e: JsonSyntaxException) {
            emptyList()  // Return an empty list if JSON parsing fails
        }
    }

    // Function to update the dictionary with previously searched words
    private fun updateDictionaryWithSearchedWords() {
        val searchedWords = getSearchedWords(userId)

        // Fetch word details based on the list of searched word IDs
        GlobalScope.launch(Dispatchers.Main) {
            val words = withContext(Dispatchers.IO) {
                val wordModels = mutableListOf<WordModel>()
                // Fetch each word by its ID
                for (wordId in searchedWords) {
                    val word = wordRepository.getWordById(wordId)
                    word?.let { wordModels.add(it) }
                }
                wordModels
            }

            // Update the adapter with the words from the searched list
            dictionaryAdapter.updateList(words)
        }
    }
}