package com.android_ai.csc13009.app.presentation.activity;

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.local.dao.QuestionDao
import com.android_ai.csc13009.app.domain.models.Answer
import com.android_ai.csc13009.app.domain.models.Lesson

interface ILearnActivity {

    var lesson: Lesson
    var currentQuestionIndex: Int
//    var questionRespository: QuestionDao
//    var lessonRepository: Repository

    public fun fetchLesson(lesson: Lesson) {
        this.lesson = lesson
    }

    public abstract fun nextLesson()

    fun submitAnswer(answer: Answer) {
        if (answer.isCorrect) {
            lesson.questionSuccess++
        }
        lesson.questions[currentQuestionIndex].isCorrect = answer.isCorrect
        nextQuestion()
    }

    fun nextQuestion() {
        currentQuestionIndex++
        if (currentQuestionIndex >= lesson.questions.size) {
//            nextLesson()
            saveProgress()
        }
    }

    fun startLesson() {
        currentQuestionIndex = 0

    }

    fun resumeLesson() {
        for (question in lesson.questions) {
            if (question.isCorrect != null) {
                nextQuestion()
            }
        }

    }

    fun saveProgress() {
        
    }

//    fun
}
