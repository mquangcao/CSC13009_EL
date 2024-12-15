package com.android_ai.csc13009.app.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android_ai.csc13009.R

class VocabularyActivity : AppCompatActivity() {
    private lateinit var btnBack : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_vocabulary)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Hooks
        btnBack = findViewById(R.id.btnBack)

        val cardLesson = findViewById<androidx.cardview.widget.CardView>(R.id.card_lesson)
        cardLesson.setOnClickListener {
            val intent = Intent(this, LearVocabActivity::class.java)
            startActivity(intent)
        }

        btnBack.setOnClickListener { finish() }
    }
}