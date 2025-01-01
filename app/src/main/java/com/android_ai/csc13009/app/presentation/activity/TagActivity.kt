package com.android_ai.csc13009.app.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.remote.model.LoginState
import com.android_ai.csc13009.app.data.remote.model.TagState
import com.android_ai.csc13009.app.data.remote.repository.FirebaseAuthRepository
import com.android_ai.csc13009.app.data.remote.repository.FirestoreTagRepository
import com.android_ai.csc13009.app.data.remote.repository.FirestoreUserRepository
import com.android_ai.csc13009.app.data.repository.TagRepository
import com.android_ai.csc13009.app.data.repository.UserRepository
import com.android_ai.csc13009.app.domain.models.Tag
import com.android_ai.csc13009.app.presentation.viewmodel.TagViewModel
import com.android_ai.csc13009.app.presentation.viewmodel.UserViewModel
import com.android_ai.csc13009.app.utils.adapter.TagAdapter
import com.android_ai.csc13009.databinding.ActivityTagBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class TagActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTagBinding
    private lateinit var tagViewModel: TagViewModel
    private lateinit var tagAdapter: TagAdapter
    private lateinit var rvTagList: RecyclerView
    private lateinit var btnBack: ImageView
    private lateinit var btnCreateTag: ImageView
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tag)

        // Initialize ViewModel
        val tagRepository = TagRepository(FirestoreTagRepository(FirebaseFirestore.getInstance()))
        tagViewModel = TagViewModel(tagRepository)

        // Initialize RecyclerView
        setupRecyclerView()

        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            // Handle user not logged in case
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show()
            return
        }
        userId = currentUser.uid
        tagViewModel.getUserTags(userId)
        // Observe tags and populate RecyclerView
        observeTags()

        // Set up click listeners
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        tagAdapter = TagAdapter(
            onTagClicked = { tag ->
                val intent = Intent(this, WordListActivity::class.java).apply {
                    putExtra("TAG_ID", tag.id)
                }
                startActivity(intent)
            },
            onDeleteClicked = { tag ->
                showDeleteConfirmationDialog(tag)
            }
        )
        rvTagList = findViewById(R.id.rvTagList)
        rvTagList.apply {
            layoutManager = LinearLayoutManager(this@TagActivity)
            adapter = tagAdapter
        }
    }

    private fun observeTags() {
        tagViewModel.tagState.observe(this) { state ->
            when (state) {
                is TagState.TagList -> {
                    // Update RecyclerView with the list of tags
                    tagAdapter.submitList(state.tags)
                }
                is TagState.Success -> {
                    // Show success message
                    Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                }
                is TagState.Error -> {
                    // Show error message
                    Toast.makeText(this, state.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupClickListeners() {
        // Handle back button click
        btnBack = findViewById(R.id.ivBack)
        btnBack.setOnClickListener {
            onBackPressed()
        }

        // Handle create tag click
        btnCreateTag = findViewById(R.id.ivCreateTag)
        btnCreateTag.setOnClickListener {
            getCurrentUserIdAndShowCreateTagDialog()
        }
    }

    private fun getCurrentUserIdAndShowCreateTagDialog() {
        showCreateTagDialog(userId)
    }

    private fun showCreateTagDialog(userId: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Create New Tag")

        // Create an EditText for user input
        val input = EditText(this)
        input.hint = "Enter tag name"
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        // Set up the dialog buttons
        builder.setPositiveButton("Create") { dialog, _ ->
            val tagName = input.text.toString().trim()
            if (tagName.isNotEmpty()) {
                tagViewModel.createTag(userId, tagName)
                tagViewModel.getUserTags(userId)
            } else {
                Toast.makeText(this, "Tag name cannot be empty!", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun showDeleteConfirmationDialog(tag: Tag) {
        AlertDialog.Builder(this)
            .setTitle("Delete Tag")
            .setMessage("Are you sure you want to delete the tag \"${tag.name}\"?")
            .setPositiveButton("Yes") { _, _ ->
                deleteTag(tag.id)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


    private fun deleteTag(tagId: String) {
        tagViewModel.deleteTag(tagId)
        tagViewModel.getUserTags(userId)
    }
}