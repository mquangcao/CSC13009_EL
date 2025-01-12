package com.android_ai.csc13009.app.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.domain.models.Word
import com.android_ai.csc13009.app.utils.adapter.WordAdapter

class ListeningQuestionFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_learn_listening, container, false)
//        val rvWord = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rv_word)
//        val words = listOf(
//            Word("Apple", "A fruit", R.drawable.apple),
//            Word("Banana", "A fruit", R.drawable.banana),
//            Word("Cherry", "A fruit", R.drawable.cherry),
//            Word("Grape", "A fruit", R.drawable.grape)
//        )
//        val adapter = WordAdapter(words)
//        val gridLayoutManager = GridLayoutManager(context, 2)
//        rvWord.layoutManager = gridLayoutManager
//        rvWord.adapter = adapter


        return view
    }
}