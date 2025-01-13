package com.android_ai.csc13009.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android_ai.csc13009.app.domain.repository.IWordRepository

class WordViewModelFactory(private val repository: IWordRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WordForTodayViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WordForTodayViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
