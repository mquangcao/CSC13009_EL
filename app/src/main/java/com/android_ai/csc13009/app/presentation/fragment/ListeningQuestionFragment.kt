package com.android_ai.csc13009.app.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.domain.models.AnswerWord
import com.android_ai.csc13009.app.domain.models.Word
import com.android_ai.csc13009.app.utils.adapter.WordAdapter
import com.android_ai.csc13009.app.utils.extensions.NavigationSetter
import com.android_ai.csc13009.app.utils.extensions.TTSSetter

class ListeningQuestionFragment : Fragment() {

    private var questionWord: Word? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_learn_listening, container, false)

        setToolbar()
        setQuestion()
        setAnswers()


        return view
    }

    private fun setToolbar() {
        val backButton = view?.findViewById<Toolbar>(R.id.learn_header_tb) ?: throw NullPointerException("Toolbar is not found")
        val skipButton = view?.findViewById<Button>(R.id.learn_header_tb_skip) ?: throw NullPointerException("Skip is not found")

        NavigationSetter.setBackButton(
            backButton,
            requireActivity()
        )

        skipButton.setOnClickListener() {
            nextQuestion()
        }

    }

    private fun setQuestion() {
        if (questionWord == null) {
            throw NullPointerException("Question word is null")
        }

        val questionPromptView = view?.findViewById<TextView>(R.id.learn_question_prompt) ?: throw NullPointerException("Question prompt is not found")
        val questionProgressBar = view?.findViewById<ProgressBar>(R.id.game_session_question_content_extra) ?: throw NullPointerException("Question progress bar is not found")
        val questionSoundButton = view?.findViewById<ImageButton>(R.id.learn_question_content) ?: throw NullPointerException("Question sound button is not found")

        questionPromptView.text = questionWord?.word ?: throw NullPointerException("Question word is null")
        TTSSetter().setTTS(questionSoundButton, questionProgressBar, questionWord?.word?: throw NullPointerException("Question word is null"), requireContext())
    }

    private fun setAnswers() {
        val answerView = view?.findViewById<RecyclerView>(R.id.rv_word)
            ?: throw NullPointerException("AnswerView is null")

        // mock data
        val words = listOf(
           AnswerWord(

           ),
        )
        val adapter = WordAdapter(words)
        val gridLayoutManager = GridLayoutManager(context, 2)
        answerView.layoutManager = gridLayoutManager

        answerView.adapter = adapter
    }

    private fun nextQuestion() {

    }
}