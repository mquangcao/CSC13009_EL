package com.android_ai.csc13009.app.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.local.AppDatabase
import com.android_ai.csc13009.app.data.remote.repository.FirestoreTagRepository
import com.android_ai.csc13009.app.data.repository.TagRepository
import com.android_ai.csc13009.app.data.repository.WordRepository
import com.android_ai.csc13009.app.domain.models.Tag
import com.android_ai.csc13009.app.domain.models.WordModel
import com.android_ai.csc13009.app.utils.adapter.WordListAdapter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WordListActivity : AppCompatActivity() {
    private lateinit var tvTagHeader: TextView
    private lateinit var wordListAdapter: WordListAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var tagRepository: TagRepository
    private lateinit var wordRepository: WordRepository
    private lateinit var btnBack: ImageView
    private lateinit var btnTagOptions: ImageView

    private lateinit var tagId: String
    private lateinit var tagName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_word_list)

        tvTagHeader = findViewById(R.id.tvTagHeader)
        tagName = intent.getStringExtra("TAG_NAME") ?: "Tag name"
        tvTagHeader.text = tagName

        btnBack = findViewById(R.id.ivBack)
        btnBack.setOnClickListener {
            setResult(RESULT_OK)  // Send result to parent activity
            finish()
            //onBackPressed()
        }

        btnTagOptions = findViewById(R.id.ivSettingTag)
        btnTagOptions.setOnClickListener {
            showTagOptionsMenu(it)
        }

        // Initialize Room Database and DAO
        val database = AppDatabase.getInstance(this)
        val wordDao = database.wordDao()
        wordRepository = WordRepository(wordDao)
        tagRepository = TagRepository(FirestoreTagRepository(FirebaseFirestore.getInstance()))


        setupRecyclerView()

        // Retrieve tagId from intent
        tagId = intent.getStringExtra("TAG_ID").toString()
        if (tagId != null) {
            fetchTag(tagId)
        } else {
            Toast.makeText(this, "Tag ID not found", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun showTagOptionsMenu(anchor: View) {
        val popupMenu = PopupMenu(this, anchor)
        val inflater: MenuInflater = popupMenu.menuInflater
        inflater.inflate(R.menu.tag_options_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_change_name -> {
                    showChangeNameDialog()
                    true
                }
                R.id.action_delete_tag -> {
                    showDeleteTagConfirmationDialog()
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun showChangeNameDialog() {
        // Inflate the custom layout
        val dialogView = layoutInflater.inflate(R.layout.dialog_change_name, null)

        // Access the views in the custom layout
        val etNewTagName = dialogView.findViewById<EditText>(R.id.etNewTagName)
        val btnSaveChangeTag = dialogView.findViewById<Button>(R.id.btnSaveChangeTag)
        val btnCancelChangeTag = dialogView.findViewById<Button>(R.id.btnCancelChangeTag)

        // Create the dialog
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        // Save button action
        btnSaveChangeTag.setOnClickListener {
            val newName = etNewTagName.text.toString().trim()
            if (newName.isNotEmpty()) {
                changeTagName(newName)
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        // Cancel button action
        btnCancelChangeTag.setOnClickListener {
            dialog.dismiss()
        }

        // Show the dialog
        dialog.show()
    }


    private fun changeTagName(newName: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                tagRepository.updateTagName(tagId, newName)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@WordListActivity, "Tag name updated", Toast.LENGTH_SHORT).show()
                    //setResult(RESULT_OK)  // Send result to parent activity
                    //finish()  // Close this activity and return to the previous one
                }
                tagName = newName
                tvTagHeader.text = tagName
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@WordListActivity, "Failed to update tag name: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showDeleteTagConfirmationDialog() {
        // Inflate the custom layout
        val dialogView = layoutInflater.inflate(R.layout.dialog_delete_tag, null)

        // Access views in the custom layout
        val tvTagMessage = dialogView.findViewById<TextView>(R.id.tvTagMessage)
        val btnConfirmDeleteTag = dialogView.findViewById<Button>(R.id.btnConfirmDeleteTag)
        val btnCancelDeleteTag = dialogView.findViewById<Button>(R.id.btnCancelDeleteTag)

        // Update the message dynamically with the tag name
        tvTagMessage.text = "Are you sure you want to delete the tag \"${tagName}\"?"

        // Create the dialog
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        // Confirm Delete button action
        btnConfirmDeleteTag.setOnClickListener {
            deleteTag()
            dialog.dismiss()
        }

        // Cancel button action
        btnCancelDeleteTag.setOnClickListener {
            dialog.dismiss()
        }

        // Show the dialog
        dialog.show()
    }

    private fun deleteTag() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                tagRepository.deleteTag(tagId)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@WordListActivity, "Tag deleted", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)  // Send result to parent activity
                    finish()  // Close this activity and return to the previous one
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@WordListActivity, "Failed to delete tag: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
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
        // Inflate the custom layout
        val dialogView = layoutInflater.inflate(R.layout.dialog_delete_confirmation, null)

        // Access views in the custom layout
        val ivWarning = dialogView.findViewById<ImageView>(R.id.ivWarning)
        val tvDeleteTitle = dialogView.findViewById<TextView>(R.id.tvDeleteTitle)
        val tvDeleteMessage = dialogView.findViewById<TextView>(R.id.tvDeleteMessage)
        val btnConfirmDelete = dialogView.findViewById<Button>(R.id.btnConfirmDelete)
        val btnCancelDelete = dialogView.findViewById<Button>(R.id.btnCancelDelete)

        // Update the message dynamically with the word ID
        tvDeleteMessage.text = "Are you sure you want to delete this word from tag?"

        // Create the dialog
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        // Confirm Delete button action
        btnConfirmDelete.setOnClickListener {
            deleteWord(wordModel)
            dialog.dismiss()
        }

        // Cancel button action
        btnCancelDelete.setOnClickListener {
            dialog.dismiss()
        }

        // Show the dialog
        dialog.show()
    }

    private fun deleteWord(wordModel: WordModel) {
        lifecycleScope.launch {
            try {
                // Remove the word from the tag in Firestore
                withContext(Dispatchers.IO) {
                    tagRepository.deleteWordFromTag(wordModel.id, tagId)
                }

                // Update UI after deletion
                wordListAdapter.removeWord(wordModel)

                Toast.makeText(this@WordListActivity, "Word successfully removed from tag.", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this@WordListActivity, "Failed to remove word: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

// In WordListAdapter
fun WordListAdapter.removeWord(wordModel: WordModel) {
    val currentList = this.currentList().toMutableList()
    currentList.remove(wordModel)
    this.updateList(currentList)
}