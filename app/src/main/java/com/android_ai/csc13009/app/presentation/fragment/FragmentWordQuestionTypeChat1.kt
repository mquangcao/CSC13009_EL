package com.android_ai.csc13009.app.presentation.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.domain.models.AnswerWord
import com.android_ai.csc13009.app.utils.ChatBubbleView
import com.android_ai.csc13009.app.utils.adapter.WordMeaningAdapter
import com.google.android.material.button.MaterialButton
import java.util.ArrayList


class FragmentWordQuestionTypeChat1(val questionTitle : String, val answerWords : ArrayList<AnswerWord>) : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_word_question_type_chat1, container, false)

        val chat_bubble = view.findViewById<ChatBubbleView>(R.id.chat_bubble)
        chat_bubble.setText(questionTitle)

        val btn_check_answer = view.findViewById<MaterialButton>(R.id.btn_check_answer)
        btn_check_answer.setOnClickListener {
            completeTask(answerWords)
        }

        val rv_word = view.findViewById<RecyclerView>(R.id.rv_word)
        rv_word.adapter = WordMeaningAdapter(answerWords)

        rv_word.layoutManager = LinearLayoutManager(context)

        return view;
    }

    private fun completeTask(data: List<AnswerWord>) {
        for (i in data.indices) {
            val result = Bundle()
            if (data[i].isSelected == true) {
                if (data[i].isCorrect) {
                    result.apply {
                        putString("result", "correct")
                    }
                } else {
                    result.apply {
                        putString("result", "in_correct")
                    }
                }

                parentFragmentManager.setFragmentResult("taskCompleted", result)
                return
            }
        }
    }
}