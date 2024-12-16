package com.android_ai.csc13009.app.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.local.entity.ChapterEntity
import com.android_ai.csc13009.app.utils.adapter.ChapterAdapter

class VocabularyActivity : AppCompatActivity() {
    private lateinit var btnBack : ImageButton
    private lateinit var recyclerView: RecyclerView

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
        recyclerView = findViewById(R.id.recyclerView)


        val list = listOf(
            ChapterEntity(1, "Hello", "Xin chào", "begin",true),
            ChapterEntity(2, "Goodbye", "Tạm biệt", "begin",true),
            ChapterEntity(3, "Thank you", "Cảm ơn", "begin",true),
            ChapterEntity(4, "Sorry", "Xin lỗi", "begin",true),
            ChapterEntity(5, "Please", "Làm ơn", "begin",true),
            ChapterEntity(6, "Yes", "Vâng", "begin",true),
            ChapterEntity(7, "No", "Không", "begin",true),
            ChapterEntity(8, "What", "Cái gì", "begin",true),
            ChapterEntity(9, "When", "Khi nào", "begin",true)
        )

        //Set adapter

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ChapterAdapter(list)



        btnBack.setOnClickListener { finish() }
    }
}