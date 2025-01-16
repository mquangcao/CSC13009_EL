package com.android_ai.csc13009.app.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.presentation.activity.GrammarActivity
import com.android_ai.csc13009.app.presentation.activity.VocabularyActivity


class LearnFragment : Fragment() {
    private lateinit var learnVocab : CardView
    private lateinit var learnGrammar : CardView
    private lateinit var book : CardView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_learn, container, false)

        learnVocab = view.findViewById(R.id.learnVocab)
        learnGrammar = view.findViewById(R.id.learnGrammar)
        book = view.findViewById(R.id.book)


        learnVocab.setOnClickListener {
            val intent = Intent(view.context, VocabularyActivity::class.java)
            startActivity(intent)
        }

        learnGrammar.setOnClickListener {
            val intent = Intent(view.context, GrammarActivity::class.java)
            startActivity(intent)
        }

        book.setOnClickListener {
            val intent = Intent(view.context, VocabularyActivity::class.java)
            startActivity(intent)
        }

        return view
    }



}