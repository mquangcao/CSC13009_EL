//package com.android_ai.csc13009.app.presentation.activity
//
//import android.os.Bundle
//import android.widget.ImageView
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.android_ai.csc13009.R
//import com.android_ai.csc13009.app.data.local.dao.LearningDetailDao
//import com.android_ai.csc13009.app.data.local.dao.LessonDao
//import com.android_ai.csc13009.app.domain.models.Lesson
//
//class LearnListeningActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_learn_listening)
//
//
//        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
//        val btnBack = findViewById<ImageView>(R.id.arrow_back)
//
//
//        recyclerView.layoutManager = LinearLayoutManager(this)
//
//        btnBack.setOnClickListener {
//            finish()
//        }
//
//    }
//
//    fun skipLesson() {
//        TODO("Not yet implemented")
//    }
//
//     fun fetchLesson() {
//        TODO("Not yet implemented")
//    }
//
//    override var lesson: Lesson
//        get() = TODO("Not yet implemented")
//        set(value) {}
//    override var currentQuestionIndex: Int
//        get() = TODO("Not yet implemented")
//        set(value) {}
//    override var lessonDao: LessonDao
//        get() = TODO("Not yet implemented")
//        set(value) {}
//    override var learningDetailDao: LearningDetailDao
//        get() = TODO("Not yet implemented")
//        set(value) {}
//    override var userId: String
//        get() = TODO("Not yet implemented")
//        set(value) {}
//
//    override fun nextLesson() {
//        TODO("Not yet implemented")
//    }
//
//     fun startLesson(lessonId: Int) {
//        TODO("Not yet implemented")
//    }
//
//     fun resumeLesson(lessonId: Int) {
//        TODO("Not yet implemented")
//    }
//
//     fun saveProgress(lessonId: Int) {
//        TODO("Not yet implemented")
//    }
//}