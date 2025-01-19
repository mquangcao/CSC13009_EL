package com.android_ai.csc13009.app.presentation.activity

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.repository.ChapterRepository
import com.android_ai.csc13009.app.domain.models.Chapters
import com.android_ai.csc13009.app.domain.repository.IChapterRepository
import com.android_ai.csc13009.app.utils.adapter.ChapterAdapter
import kotlinx.coroutines.launch

class VocabularyActivity : AppCompatActivity() {
    private lateinit var btnBack : ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var  chapterRepository : IChapterRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_vocabulary)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //init
        chapterRepository = ChapterRepository(this)

        //Hooks
        btnBack = findViewById(R.id.btnBack)
        recyclerView = findViewById(R.id.recyclerView)


        updateUI()

        btnBack.setOnClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    private fun updateUI() {
        lifecycleScope.launch {
            val list = chapterRepository.getChapterList()
            val data = list.filter { it.totalLesson > 0 }
            // Cập nhật UI sau khi lấy danh sách
            updateUI(data)
        }
    }

    private fun updateUI(list: List<Chapters>) {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ChapterAdapter(list)

        recyclerView.post {
            val totalHeight = recyclerView.adapter?.itemCount?.let { count ->
                val itemHeight = recyclerView.getChildAt(0)?.height ?: 0
                count * itemHeight + 42 * (count - 1)
            }
            recyclerView.layoutParams.height = totalHeight ?: RecyclerView.LayoutParams.WRAP_CONTENT
            recyclerView.requestLayout()
        }
    }
}