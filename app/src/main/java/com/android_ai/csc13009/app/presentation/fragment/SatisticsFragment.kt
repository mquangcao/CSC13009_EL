package com.android_ai.csc13009.app.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android_ai.csc13009.databinding.FragmentStatisticsBinding
import com.mikhaellopez.circularprogressbar.CircularProgressBar

class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val PROGRESS_MAX = 100f
    }

    data class ProgressData(
        val title: String,
        val progress: Int,
        val description: String,
        val color: String
    )

    private val progressDataList = listOf(
        ProgressData("Vocab", 70, "70% success vocab", "#FC8890"),
        ProgressData("Listening", 33, "33% success vocab", "#CEB7D4"),
        ProgressData("Grammar", 10, "10% success vocab", "#0EADD2")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupProgressBars()
    }

    private fun setupProgressBars() {
        val progressBars = listOf(
            binding.circularProgressBar to progressDataList[0],
            binding.circularProgressBar2 to progressDataList[1],
            binding.circularProgressBar3 to progressDataList[2]
        )

        for ((progressBar, data) in progressBars) {
            updateProgressBar(progressBar, data)
        }

        // Update TextViews
        binding.tvWord1.text = progressDataList[0].title
        binding.tvDescription1.text = progressDataList[0].description

        binding.tvWord2.text = progressDataList[1].title
        binding.tvDescription2.text = progressDataList[1].description

        binding.tvWord3.text = progressDataList[2].title
        binding.tvDescription3.text = progressDataList[2].description
    }


    private fun updateProgressBar(progressBar: CircularProgressBar, data: ProgressData) {
        progressBar.progress = data.progress.toFloat()
        progressBar.progressMax = PROGRESS_MAX
        progressBar.progressBarColor = safeParseColor(data.color, android.graphics.Color.GRAY)
    }

    private fun safeParseColor(colorString: String, defaultColor: Int): Int {
        return try {
            android.graphics.Color.parseColor(colorString)
        } catch (e: IllegalArgumentException) {
            defaultColor
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
