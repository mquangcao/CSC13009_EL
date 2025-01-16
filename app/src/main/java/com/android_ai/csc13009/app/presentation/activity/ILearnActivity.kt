//package com.android_ai.csc13009.app.presentation.activity;
//
//import com.android_ai.csc13009.app.data.local.dao.LearningDetailDao
//import com.android_ai.csc13009.app.data.local.dao.LessonDao
//import com.android_ai.csc13009.app.domain.models.Answer
//import com.android_ai.csc13009.app.domain.models.Lesson
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//
//interface ILearnActivity {
//
//    var lesson: Lesson
//    var currentQuestionIndex: Int
//    var lessonDao: LessonDao
//    var learningDetailDao: LearningDetailDao
//    var userId: String
//
//    fun submitAnswer(answer: Answer) {
//        if (answer.isCorrect) {
////            lesson.questionSuccess++
//            // update
//        }
////        lesson.questions[currentQuestionIndex].isCorrect = answer.isCorrect
//
//        CoroutineScope(Dispatchers.IO).launch {
//            val learningDetail = learningDetailDao.getLearningDetailByQuestionAndUser(lesson.questions[currentQuestionIndex].id, userId)
//            learningDetail.isCorrect = answer.isCorrect
//            learningDetailDao.insertLearningDetail(learningDetail)
//        }
//        nextQuestion()
//    }
//
//    fun nextQuestion() {
//        currentQuestionIndex++
//        if (currentQuestionIndex >= lesson.questions.size) {
////            nextLesson()
////            saveProgress()
//        }
//    }
//
//    fun startLesson() {
//        currentQuestionIndex = 0
//
//    }
//
//
////    fun resumeLesson() {
////        for (question in lesson.questions) {
////            if (question.isCorrect != null) {
////                nextQuestion()
////            }
////        }
////
////    }
//
////    fun saveProgress() {
////
////    }
//
////    fun
//}
