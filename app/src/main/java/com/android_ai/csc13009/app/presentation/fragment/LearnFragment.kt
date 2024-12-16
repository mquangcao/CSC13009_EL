package com.android_ai.csc13009.app.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.presentation.activity.VocabularyActivity


class LearnFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_learn, container, false)
        val intent = Intent(view.context, VocabularyActivity::class.java)
        startActivity(intent)

        return view
    }



}