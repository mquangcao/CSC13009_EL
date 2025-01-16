package com.android_ai.csc13009.app.presentation.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.local.AppDatabase
import com.android_ai.csc13009.app.data.repository.WordRepository
import com.android_ai.csc13009.app.domain.models.WordSchedule
import com.android_ai.csc13009.app.utils.adapter.DictionaryAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AddWordDialogFragment : DialogFragment() {

    private var onWordAddedListener: ((WordSchedule) -> Unit)? = null

    private lateinit var searchBar: AutoCompleteTextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnAdd: Button
    private lateinit var btnCancel: Button

    private lateinit var dictionaryAdapter: DictionaryAdapter
    private lateinit var wordRepository: WordRepository

    fun setOnWordAddedListener(listener: (WordSchedule) -> Unit) {
        onWordAddedListener = listener
    }

    private var selectedWordId: Int? = null // Variable to hold the selected word ID

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.fragment_add_word_dialog, null)

        searchBar = dialogView.findViewById(R.id.etSearch)
        recyclerView = dialogView.findViewById(R.id.rvSearchResults)
        btnAdd = dialogView.findViewById(R.id.btnAdd)
        btnCancel = dialogView.findViewById(R.id.btnCancel)

        // Initialize Room Database and DAO
        val database = AppDatabase.getInstance(requireContext())
        val wordDao = database.wordDao()
        wordRepository = WordRepository(wordDao)

        setupRecyclerView()
        setupSearchBar()
        setupButtons()

        builder.setView(dialogView)
        return builder.create()
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        dictionaryAdapter = DictionaryAdapter(emptyList()) { wordModel ->
            // Set the selected word in the search bar
            searchBar.setText(wordModel.word)

            // Store the selected word's ID
            selectedWordId = wordModel.id

            // Hide the RecyclerView
            recyclerView.visibility = View.GONE

            // Remove focus from the search bar
            searchBar.clearFocus()

            // Optionally, hide the keyboard
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(searchBar.windowToken, 0)
        }
        recyclerView.adapter = dictionaryAdapter
    }

    private fun setupSearchBar() {
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    recyclerView.visibility = View.GONE
                } else {
                    recyclerView.visibility = View.VISIBLE
                    searchWords(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun searchWords(query: String) {
        GlobalScope.launch(Dispatchers.Main) {
            val words = withContext(Dispatchers.IO) {
                wordRepository.getSuggestions(query)
            }

            dictionaryAdapter.updateList(words)
        }
    }

    private fun setupButtons() {
        btnAdd.setOnClickListener {
            // Check if a word has been selected
            if (selectedWordId == null) {
                Toast.makeText(context, "Please select a word", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create a WordSchedule using the selected word's ID
            val wordSchedule = WordSchedule(
                wordId = selectedWordId!!,
                lastReviewed = System.currentTimeMillis(),
                nextReview = System.currentTimeMillis() + 86400000, // Default next review: 1 day later
                reviewCount = 0,
                successRate = 0.0f
            )
            Log.d("DatabaseCheck", "Schedule: $wordSchedule")
            onWordAddedListener?.invoke(wordSchedule)
            dismiss()
        }

        btnCancel.setOnClickListener {
            dismiss()
        }
    }
}

