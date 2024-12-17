package com.android_ai.csc13009.app.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.local.AppDatabase
import com.android_ai.csc13009.app.data.local.repository.WordRepository
import com.android_ai.csc13009.app.presentation.viewmodel.WordForTodayViewModel
import com.android_ai.csc13009.app.presentation.viewmodel.WordViewModelFactory
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest

class HomeFragment : Fragment() {

    private val wordViewModel: WordForTodayViewModel by viewModels {
        WordViewModelFactory(
            WordRepository(AppDatabase.getInstance(requireContext()).wordDao()) // Sử dụng requireContext()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val tvWord = view.findViewById<TextView>(R.id.tvWord)
        val tvPronunciation = view.findViewById<TextView>(R.id.tvPronunciation)

        lifecycleScope.launchWhenStarted {
            wordViewModel.wordForToday.collectLatest { word ->
                word?.let {
                    tvWord.text = it.word
                    tvPronunciation.text = it.pronunciation?.trim() ?: "N/A"// Xử lý khi pronunciation null

                }
            }
        }

        wordViewModel.fetchRandomWord()

        return view
    }


}