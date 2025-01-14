package com.android_ai.csc13009.app.presentation.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.domain.models.AnswerWord
import com.android_ai.csc13009.app.domain.models.Question
import com.android_ai.csc13009.app.utils.ChatBubbleView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.util.ArrayList
import java.util.Locale

class FragmentWordQuestionTranslate(val questionTitle : String, val answerWords : ArrayList<AnswerWord>) : Fragment() {
    private lateinit var etAnswerInput : TextInputEditText
    private lateinit var chatBubble : ChatBubbleView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_word_question_translate, container, false)
        val btnMic: ImageButton = view.findViewById(R.id.btn_mic)
        etAnswerInput = view.findViewById(R.id.et_answer_input)
        chatBubble = view.findViewById(R.id.chat_bubble)

        chatBubble.setText(questionTitle)

        btnMic.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH)
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now")
            startActivityForResult(intent, 100)
        }

        val btn_check_answer = view.findViewById<MaterialButton>(R.id.btn_check_answer)
        btn_check_answer.setOnClickListener {
            completeTask(answerWords)
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 100 || data != null) {
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            etAnswerInput.setText(result?.get(0))
        }
    }

    private fun completeTask(data: List<AnswerWord>) {
        for (i in data.indices) {
            val result = Bundle()
            if (etAnswerInput.text.toString().toLowerCase() == data[i].text.toLowerCase()) {
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
