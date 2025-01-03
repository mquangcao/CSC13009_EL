package com.android_ai.csc13009.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android_ai.csc13009.app.domain.repository.model.Word
import com.android_ai.csc13009.app.domain.repository.repository.IWordRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WordForTodayViewModel(private val repository: IWordRepository) : ViewModel() {

    private val _wordForToday = MutableStateFlow<Word?>(null)
    val wordForToday: StateFlow<Word?> = _wordForToday

    fun fetchRandomWord() {
        viewModelScope.launch {
            val word = withContext(Dispatchers.IO) {
                repository.getRandomWord()
            }
            _wordForToday.value = word
        }
    }
}
