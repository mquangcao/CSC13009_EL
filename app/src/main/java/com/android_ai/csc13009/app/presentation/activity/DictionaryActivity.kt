package com.android_ai.csc13009.app.presentation.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AutoCompleteTextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.local.AppDatabase
import com.android_ai.csc13009.app.data.local.repository.WordRepository
import com.android_ai.csc13009.app.domain.repository.model.Word
import com.android_ai.csc13009.app.presentation.adapter.WordAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DictionaryActivity : AppCompatActivity() {
    private lateinit var etSearch: AutoCompleteTextView
    private lateinit var rvSearchResults: RecyclerView
    private lateinit var wordAdapter: WordAdapter

    private val wordList = mutableListOf<Word>()
    private lateinit var wordRepository: WordRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dictionary)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Khởi tạo Room Database và DAO
        val database = AppDatabase.getInstance(this)
        val wordDao = database.wordDao()
        wordRepository = WordRepository(wordDao)

        // Khởi tạo AutoCompleteTextView
        etSearch = findViewById(R.id.etSearch)
        rvSearchResults = findViewById(R.id.rvSearchResults)

        // Thiết lập RecyclerView
        rvSearchResults.layoutManager = LinearLayoutManager(this)
        wordAdapter = WordAdapter(wordList)
        rvSearchResults.adapter = wordAdapter

        // Lắng nghe sự thay đổi trong ô tìm kiếm
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    rvSearchResults.visibility = View.GONE
                } else {
                    rvSearchResults.visibility = View.VISIBLE
                    searchWords(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    // Hàm tìm kiếm từ vựng khi người dùng nhập
    private fun searchWords(query: String) {
        GlobalScope.launch(Dispatchers.Main) {
            val words = withContext(Dispatchers.IO) {
                wordRepository.getSuggestions(query)
            }

            wordAdapter.updateList(words)
        }
    }
}