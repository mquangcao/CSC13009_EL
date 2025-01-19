package com.android_ai.csc13009.app.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.domain.models.ListeningAnswer
import com.android_ai.csc13009.app.utils.adapter.ListeningAnswersAdapter
import com.android_ai.csc13009.app.utils.extensions.TTSSetter
import com.google.android.material.button.MaterialButton

class ListeningQuestionFragment(val questionId : String, val questionTitle : String, val answerWords : ArrayList<ListeningAnswer>, val audioTranscript: String) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_learn_listening, container, false)

        val prompt = view.findViewById<TextView>(R.id.learn_question_prompt)
        prompt.text = questionTitle

        val audioButton = view.findViewById<ImageButton>(R.id.learn_question_content)
        val audioProgress = view.findViewById<ProgressBar>(R.id.game_session_question_content_extra)
        val ttsSetter = TTSSetter()
        ttsSetter.setTTS(audioButton, audioProgress, audioTranscript, requireContext())

        val checkAnswerButton = view.findViewById<MaterialButton>(R.id.btn_check_answer)
        checkAnswerButton.setOnClickListener {
            completeTask(answerWords)
        }

        val answerRecyclerView = view.findViewById<RecyclerView>(R.id.rv_word)
        answerRecyclerView.adapter = ListeningAnswersAdapter(answerWords)
        answerRecyclerView.layoutManager = LinearLayoutManager(context)

        return view
//        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun completeTask(data: List<ListeningAnswer>) {
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
                result.apply {
                    putString("questionId", questionId)
                }

                parentFragmentManager.setFragmentResult("taskCompleted", result)
                return
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}
