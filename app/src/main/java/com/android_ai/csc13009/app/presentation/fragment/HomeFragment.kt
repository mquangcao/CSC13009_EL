package com.android_ai.csc13009.app.presentation.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import com.android_ai.csc13009.R
import com.android_ai.csc13009.app.data.local.AppDatabase
import com.android_ai.csc13009.app.data.repository.WordRepository
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
            wordViewModel.wordModelForToday.collectLatest { word ->
                word?.let {
                    tvWord.text = it.word
                    tvPronunciation.text = it.pronunciation?.trim() ?: "N/A"// Xử lý khi pronunciation null

                }
            }
        }

        wordViewModel.fetchRandomWord()

        if (savedInstanceState == null) {
            val fragment = StatisticsFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.statisticsFragmentContainer, fragment)
                .commit()
        }

        // Set default fragment (Daily)
        if (savedInstanceState == null) {
            val fragment = DailyStatisticsFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit()
        }

        // Get references to buttons
        val btnDaily: Button = view.findViewById(R.id.btnDaily)
        val btnMonthly: Button = view.findViewById(R.id.btnMonthly)

        // Set initial button styles and highlight Daily by default
        setButtonDefaultStyle(btnDaily, btnMonthly)
        setButtonSelectedStyle(btnDaily, btnMonthly)

        // Set Daily button click listener
        btnDaily.setOnClickListener {
            val fragment = DailyStatisticsFragment()
            switchFragment(fragment)
            setButtonSelectedStyle(btnDaily, btnMonthly)
        }

        // Set Monthly button click listener
        btnMonthly.setOnClickListener {
            val fragment = MonthlyStatisticsFragment()
            switchFragment(fragment)
            setButtonSelectedStyle(btnMonthly, btnDaily)
        }

        return view
    }

    private fun switchFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }

    private fun setButtonDefaultStyle(btnDaily: Button, btnMonthly: Button) {
        btnDaily.setBackgroundColor(Color.parseColor("#FFFFFF"))
        btnDaily.setTextColor(Color.parseColor("#000000"))
        btnMonthly.setBackgroundColor(Color.parseColor("#FFFFFF"))
        btnMonthly.setTextColor(Color.parseColor("#000000"))
    }

    private fun setButtonSelectedStyle(selectedButton: Button, unselectedButton: Button) {
        selectedButton.setBackgroundColor(Color.parseColor("#FC8890"))
        selectedButton.setTextColor(Color.parseColor("#FFFFFF"))
        unselectedButton.setBackgroundColor(Color.parseColor("#FFFFFF"))
        unselectedButton.setTextColor(Color.parseColor("#000000"))
    }
}