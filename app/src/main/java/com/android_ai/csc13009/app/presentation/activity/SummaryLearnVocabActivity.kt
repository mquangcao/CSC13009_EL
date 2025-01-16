package com.android_ai.csc13009.app.presentation.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android_ai.csc13009.R

class SummaryLearnVocabActivity : AppCompatActivity() {
    private lateinit var btnFinish : Button
    private lateinit var tvTime : TextView
    private lateinit var tvCorrect : TextView
    private lateinit var tvTitle : TextView
    private lateinit var ivThumb : ImageView

    private lateinit var slogans : List<String>
    private var imgThumb = listOf(
        R.drawable.img_thumb_accout,
        R.drawable.thumb_chapter_activity,
        R.drawable.thumb_learn_activity
    )

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_summary_learn_vocab)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        slogans = resources.getStringArray(R.array.practice_slogans).toList()

        // Hooks
        ivThumb = findViewById(R.id.ivThumb)
        tvTitle = findViewById(R.id.tvTitle)

        val intent = intent
        val time = intent.getStringExtra("time")
        val correctAnswer = intent.getIntExtra("correctAnswer", 0)
        when {
            correctAnswer < 50 -> {
                tvTitle.text = slogans[0]
            }
            correctAnswer < 75 -> {
                tvTitle.text = slogans[1]
            }
            else -> {
                tvTitle.text = slogans[2]
            }
        }

        val r = (0..2).random()
        ivThumb.setImageResource(imgThumb[r])


        tvTime = findViewById(R.id.tvTime)
        tvTime.text = time

        tvCorrect = findViewById(R.id.tvCorrect)
        tvCorrect.text = "$correctAnswer%"

        btnFinish = findViewById(R.id.btnFinish)
        btnFinish.setOnClickListener {
            finish()
        }
    }
}