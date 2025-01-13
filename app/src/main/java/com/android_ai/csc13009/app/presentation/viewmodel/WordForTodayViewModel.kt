package com.android_ai.csc13009.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android_ai.csc13009.app.domain.models.WordModel
import com.android_ai.csc13009.app.domain.repository.IWordRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WordForTodayViewModel(private val repository: IWordRepository) : ViewModel() {

    private val _wordModelForToday = MutableStateFlow<WordModel?>(null)
    val wordModelForToday: StateFlow<WordModel?> = _wordModelForToday

    fun fetchRandomWord() {
        viewModelScope.launch {
            val word = withContext(Dispatchers.IO) {
                repository.getRandomWord()
            }
            _wordModelForToday.value = word
        }
    }
}
