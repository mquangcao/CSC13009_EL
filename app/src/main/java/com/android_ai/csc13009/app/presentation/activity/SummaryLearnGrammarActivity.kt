package com.android_ai.csc13009.app.presentation.activity

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android_ai.csc13009.R

class SummaryLearnGrammarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_summary_learn_grammar)

        // Retrieve the passed data
        val correctAnswers = intent.getIntExtra("CORRECT_ANSWERS", 0)
        val incorrectAnswers = intent.getIntExtra("INCORRECT_ANSWERS", 0)

        // Set the retrieved data to the corresponding TextViews
        val correctTextView = findViewById<TextView>(R.id.tvCorrectAnswers)
        val incorrectTextView = findViewById<TextView>(R.id.tvIncorrectAnswers)

        correctTextView.text = "$correctAnswers"
        incorrectTextView.text = "$incorrectAnswers"

        // Adjust padding for edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Handle the "Finish" button click
        val btnfinish = findViewById<com.google.android.material.button.MaterialButton>(R.id.btnFinish)

        btnfinish.setOnClickListener {
            finish()
        }
    }
}