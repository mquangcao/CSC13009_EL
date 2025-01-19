package com.android_ai.csc13009.app.presentation.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android_ai.csc13009.app.domain.models.WordModel
import com.android_ai.csc13009.app.domain.repository.IWordRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class WordForTodayViewModel(
    private val repository: IWordRepository,
    private val preferences: SharedPreferences // Thêm SharedPreferences
) : ViewModel() {

    private val _wordModelForToday = MutableStateFlow<WordModel?>(null)
    val wordModelForToday: StateFlow<WordModel?> = _wordModelForToday

    fun fetchRandomWord() {
        viewModelScope.launch {
            val today = getCurrentDate()
            val savedWordId = preferences.getInt("word_for_today_id", -1)
            val savedWord = preferences.getString("word_for_today", null)
            val savedPronunciation = preferences.getString("word_pronunciation", null)
            val savedDetails = preferences.getString("word_details", null)
            val savedDate = preferences.getString("word_date", null)

            if (savedDate == today && savedWord != null && savedWordId != -1) {
                // Nếu từ của ngày hiện tại đã tồn tại
                _wordModelForToday.value = WordModel(
                    id = savedWordId,
                    word = savedWord,
                    pronunciation = savedPronunciation,
                    details = savedDetails ?: ""
                )
            } else {
                // Nếu chưa có, fetch từ mới
                val word = withContext(Dispatchers.IO) {
                    repository.getRandomWord()
                }
                _wordModelForToday.value = word
                if (word != null) {
                    preferences.edit()
                        .putInt("word_for_today_id", word.id)
                        .putString("word_for_today", word.word)
                        .putString("word_pronunciation", word.pronunciation)
                        .putString("word_details", word.details)
                        .putString("word_date", today)
                        .apply()
                }
            }
        }
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }
}
