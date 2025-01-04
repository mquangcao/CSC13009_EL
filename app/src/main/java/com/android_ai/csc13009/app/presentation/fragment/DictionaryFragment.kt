package com.android_ai.csc13009.app.presentation.fragment

import android.content.Intent
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

        rvSearchResults.layoutManager = LinearLayoutManager(requireContext())
        dictionaryAdapter = DictionaryAdapter(emptyList()) { wordModel ->
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


        // Setup RecyclerView
        //rvSearchResults.layoutManager = LinearLayoutManager(requireContext())
        //dictionaryAdapter = DictionaryAdapter(wordList)
        //rvSearchResults.adapter = dictionaryAdapter

        // Listen for text changes in the search bar
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
}