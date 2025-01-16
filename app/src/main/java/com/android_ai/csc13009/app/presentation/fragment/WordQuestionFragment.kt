package com.android_ai.csc13009.app.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.domain.models.AnswerWord
import com.android_ai.csc13009.app.domain.models.Word
import com.android_ai.csc13009.app.utils.adapter.WordAdapter
import java.util.ArrayList

class WordQuestionFragment(val questionId : String,val questionTitle : String, val answerWords : ArrayList<AnswerWord>) : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_word_question, container, false)
        val rvWord = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rv_word)

        val tvWord = view.findViewById<android.widget.TextView>(R.id.tv_word)
        tvWord.setText(questionTitle)

//        val words = listOf(
//            AnswerWord().apply {
//                text = "Apple"
//                imgUrl = "https://res.cloudinary.com/dxz9cc66y/image/upload/v1736702298/yedkb7haocnegtuj81my.jpg"
//                isCorrect = false
//            },
//            AnswerWord().apply {
//                text = "Banana"
//                imgUrl = "https://res.cloudinary.com/dxz9cc66y/image/upload/v1736702308/et91qrxklhxofkjsdso8.jpg"
//                isCorrect = true
//            },
//            AnswerWord().apply {
//                text = "Cherry"
//                imgUrl = "https://res.cloudinary.com/dxz9cc66y/image/upload/v1736702313/b3rsserymwvzsfqg0uvn.png"
//                isCorrect = false
//            },
//            AnswerWord().apply {
//                text = "Grape"
//                imgUrl = "https://res.cloudinary.com/dxz9cc66y/image/upload/v1736702319/ecfjhtbgsf26dazwceiz.jpg"
//                isCorrect = false
//            }
//        )
        val adapter = WordAdapter(answerWords)
        val gridLayoutManager = GridLayoutManager(context, 2)
        rvWord.layoutManager = gridLayoutManager
        rvWord.adapter = adapter

        val btn_check_answer = view.findViewById<com.google.android.material.button.MaterialButton>(R.id.btn_check_answer)
        btn_check_answer.setOnClickListener {
            completeTask()
        }


        return view
    }

    private fun completeTask() {
        for (i in answerWords.indices) {
            val result = Bundle()
            if (answerWords[i].isSelected == true) {
                if (answerWords[i].isCorrect) {
                    result.apply {
                        putString("result", "correct")
                    }
                } else {
                    result.apply {
                        putString("result", "in_correct")
                    }
                }
                result.apply {
                    putString("questionId", questionId)
                }

                parentFragmentManager.setFragmentResult("taskCompleted", result)
                return
            }
        }
    }


}