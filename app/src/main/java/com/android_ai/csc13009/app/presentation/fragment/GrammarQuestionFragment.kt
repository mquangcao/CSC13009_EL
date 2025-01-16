package com.android_ai.csc13009.app.presentation.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.domain.models.GrammarAnswer
import com.android_ai.csc13009.app.domain.models.GrammarQuestion
import com.android_ai.csc13009.app.utils.adapter.GrammarAnswerAdapter
import com.google.android.material.button.MaterialButton

class GrammarQuestionFragment : Fragment() {

    private lateinit var question: GrammarQuestion
    private lateinit var answers: List<GrammarAnswer>
    private var onAnswerSelected: ((Boolean) -> Unit)? = null

    companion object {
        fun newInstance(
            question: GrammarQuestion,
            answers: List<GrammarAnswer>,
            onAnswerSelected: (Boolean) -> Unit
        ): GrammarQuestionFragment {
            val fragment = GrammarQuestionFragment()
            fragment.question = question
            fragment.answers = answers
            fragment.onAnswerSelected = onAnswerSelected
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_grammar_question, container, false)

        val questionText = view.findViewById<TextView>(R.id.questionText)
        val answerRecyclerView = view.findViewById<RecyclerView>(R.id.answerRecyclerView)

        questionText.text = question.name

        // Shuffle answers
        val shuffledAnswers = answers.shuffled()

        // Set up RecyclerView
        answerRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        answerRecyclerView.adapter = GrammarAnswerAdapter(requireContext(), shuffledAnswers) { selectedAnswer ->
            showAnswerDialog(selectedAnswer.isCorrect, selectedAnswer.answer)
        }

        return view
    }

    private fun showAnswerDialog(isCorrect: Boolean, selectedAnswer: String) {
        val dialogView = if (isCorrect) {
            layoutInflater.inflate(R.layout.custom_dialog_answer_correct, null)
        } else {
            layoutInflater.inflate(R.layout.custom_dialog_answer_uncorrect, null)
        }

        val answerTextView = dialogView.findViewById<TextView>(R.id.tv_content)

        if (!isCorrect) {
            // Hiển thị đáp án đúng khi chọn sai
            val correctAnswer = answers.firstOrNull { it.isCorrect }
            if (correctAnswer != null) {
                answerTextView.text = correctAnswer.answer
            } else {
                // Xử lý trường hợp không có câu trả lời đúng
                answerTextView.text = "No correct answer found."
            }
        }

        val builder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)

        val dialog = builder.create()

        // Hiển thị dialog tại phía dưới màn hình
        dialog.window?.apply {
            setGravity(Gravity.BOTTOM)
            attributes = attributes.apply {
                width = WindowManager.LayoutParams.MATCH_PARENT
                height = WindowManager.LayoutParams.WRAP_CONTENT
            }
        }

        dialog.show()

        dialogView.findViewById<MaterialButton>(R.id.btn_continue).setOnClickListener {
            dialog.dismiss()
            onAnswerSelected?.invoke(isCorrect)
        }
    }
}
