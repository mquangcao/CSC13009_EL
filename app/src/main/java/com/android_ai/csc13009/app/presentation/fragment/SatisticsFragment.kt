package com.android_ai.csc13009.app.presentation.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.android_ai.csc13009.app.data.remote.repository.FirestoreLearningDetailRepository
import com.android_ai.csc13009.databinding.FragmentStatisticsBinding
import com.google.firebase.auth.FirebaseAuth
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import kotlinx.coroutines.launch
import com.google.firebase.firestore.FirebaseFirestore

class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!
    private lateinit var userId: String

    companion object {
        const val PROGRESS_MAX = 100f
    }

    data class ProgressData(
        val title: String,
        val progress: Int,
        val description: String,
        val color: String
    )

    private val progressDataList = mutableListOf(
        ProgressData("Vocabulary", 0, "Success for Vocabulary", "#FC8890"),
        ProgressData("Listening", 0, "Success for Listening", "#CEB7D4"),
        ProgressData("Grammar", 0, "Success for Grammar", "#0EADD2")
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
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            // Handle user not logged in case
            Toast.makeText(requireContext(), "User not logged in.", Toast.LENGTH_SHORT).show()
            return
        }
        userId = currentUser.uid
        Log.d("StatisticsFragment", "User ID: $userId")
        fetchSuccessRates()
    }

    private fun fetchSuccessRates() {
        val repository = FirestoreLearningDetailRepository(FirebaseFirestore.getInstance())

        lifecycleScope.launch {
            try {
                // Fetch success rates for today
                val vocabRateToday = repository.getSuccessRateByTypeForToday(userId, "vocabulary")
                Log.d("StatisticsFragment", "Vocabulary success $vocabRateToday")
                val listeningRateToday = repository.getSuccessRateByTypeForToday(userId, "listening")
                Log.d("StatisticsFragment", "Listening success $listeningRateToday")
                val grammarRateToday = repository.getSuccessRateByTypeForToday(userId, "grammar")
                Log.d("StatisticsFragment", "Grammar success $grammarRateToday")

                // Update the progressDataList with fetched values
                updateProgressData(vocabRateToday, listeningRateToday, grammarRateToday)
                setupProgressBars()
            } catch (e: Exception) {
                // Handle error (optional)
            }
        }
    }

    private fun updateProgressData(vocabRate: Float, listeningRate: Float, grammarRate: Float) {
        progressDataList[0] = progressDataList[0].copy(
            progress = vocabRate.toInt(),
            description = "${vocabRate.toInt()}% success"
        )
        progressDataList[1] = progressDataList[1].copy(
            progress = listeningRate.toInt(),
            description = "${listeningRate.toInt()}% success"
        )
        progressDataList[2] = progressDataList[2].copy(
            progress = grammarRate.toInt(),
            description = "${grammarRate.toInt()}% success"
        )
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
