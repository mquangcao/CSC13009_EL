package com.android_ai.csc13009.app.presentation.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.domain.repository.model.WordDetailItem
import com.android_ai.csc13009.app.utils.adapter.WordDetailAdapter
import org.w3c.dom.Text

class WordDetailActivity : AppCompatActivity() {
    private lateinit var tvWordHeader: TextView
    private lateinit var tvWordName: TextView
    private lateinit var tvPronunciation: TextView
    private lateinit var backButton: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var wordDetailAdapter: WordDetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_word_detail)

        tvWordHeader = findViewById(R.id.tvWordHeader)
        tvWordName = findViewById(R.id.tvWord)
        tvPronunciation = findViewById(R.id.tvPronunciation)

        backButton = findViewById(R.id.ivBack)
        backButton.setOnClickListener {
            finish()
        }

        // Đặt RecyclerView
        recyclerView = findViewById(R.id.rvWordDetails)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val wordText = intent.getStringExtra("word_text")
        val wordPronunciation = intent.getStringExtra("word_pronunciation")
        val wordDetails = intent.getStringExtra("word_details")

        tvWordHeader.text = wordText
        tvWordName.text = wordText
        tvPronunciation.text = wordPronunciation

        // Phân tích dữ liệu
        val wordDetailItems: List<WordDetailItem> = parseRawData(wordDetails)

        // Khởi tạo và thiết lập adapter
        wordDetailAdapter = WordDetailAdapter(wordDetailItems)
        recyclerView.adapter = wordDetailAdapter
    }

    private fun parseRawData(rawData: String?): List<WordDetailItem> {
        val items = mutableListOf<WordDetailItem>()
        // Split the input string by '|'
        val parts = rawData?.split("|")

        // Iterate through each part and determine its type
        if (parts != null) {
            for (part in parts) {
                if (part != "") {
                    val trimmedPart = part.trim()

                    when {
                        trimmedPart.startsWith("*") -> {
                            items.add(WordDetailItem("*", trimmedPart.substring(2).trim()))
                        }

                        trimmedPart.startsWith("-") -> {
                            items.add(WordDetailItem("-", trimmedPart.substring(2).trim()))
                        }

                        trimmedPart.startsWith("=") -> {
                            items.add(WordDetailItem("=", trimmedPart.substring(1).trim()))
                        }

                        trimmedPart.startsWith("+") -> {
                            items.add(WordDetailItem("+", trimmedPart.substring(2).trim()))
                        }

                        else -> {
                            items.add(WordDetailItem("*", trimmedPart.trim()))
                        }
                    }
                }
            }
        }

        return items
    }
}