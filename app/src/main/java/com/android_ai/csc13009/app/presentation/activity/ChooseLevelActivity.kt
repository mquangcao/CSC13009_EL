package com.android_ai.csc13009.app.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.remote.repository.FirestoreUserRepository
import com.android_ai.csc13009.app.domain.models.Level
import com.android_ai.csc13009.app.presentation.service.SyncDataFromFirestore
import com.android_ai.csc13009.app.utils.ChatBubbleView
import com.android_ai.csc13009.app.utils.adapter.ChooseLevelAdapter
import com.google.android.material.button.MaterialButton
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class ChooseLevelActivity : AppCompatActivity() {

    private lateinit var chatBubble : ChatBubbleView
    private lateinit var rvWord : RecyclerView
    private lateinit var btnCheckAnswer : MaterialButton
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_choose_level)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        hooks()

        setUp()
    }

    private val listData = listOf(
        Level("beginner", "Tôi mới học tiếng anh", R.drawable.brightness_5_24px),
        Level("intermediate", "Tôi đã có thể giao tiếp cơ bản", R.drawable.brightness_6_24px),
        Level("advanced", "Tôi đã có thể thảo luận tốt về các vấn đề cụ thể", R.drawable.brightness_7_24px)
    )

    private fun genData() = listData

    private fun setUp() {
        chatBubble.setText("Trình độ tiếng anh của bạn ở mức?")
        rvWord.adapter = ChooseLevelAdapter(genData())
        rvWord.layoutManager = LinearLayoutManager(this)


        btnCheckAnswer.setOnClickListener {
            Log.d("ChooseLevelActivity", "Check answer")
            val level = listData.find { it.isSelected }

            val firestore = FirebaseFirestore.getInstance()

            if (level == null) return@setOnClickListener

            progressBar.visibility = View.VISIBLE
            btnCheckAnswer.isEnabled = false

            lifecycleScope.launch {
                try {
                    val userId = intent.getStringExtra("userId") ?: ""

                    // Thực hiện các tác vụ tốn thời gian
                    val data = SyncDataFromFirestore(firestore, this@ChooseLevelActivity)
                    data.run()
                    data.fetchDataLevel(level.id)

                    FirestoreUserRepository(firestore).updateLevel(userId, level.id)

                    // Chuyển sang DashboardActivity
                    val intent = Intent(this@ChooseLevelActivity, DashboardActivity::class.java)
                    startActivity(intent)
                    finish()
                } finally {
                    // Ẩn ProgressBar
                    progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun hooks() {
        chatBubble = findViewById(R.id.chat_bubble)
        rvWord = findViewById(R.id.rv_word)
        btnCheckAnswer = findViewById(R.id.btn_check_answer)
        progressBar = findViewById(R.id.progress_bar)
    }
}