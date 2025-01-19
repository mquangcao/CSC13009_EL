package com.android_ai.csc13009.app.presentation.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android_ai.csc13009.app.domain.repository.IWordRepository

class WordViewModelFactory(
    private val repository: IWordRepository,
    private val preferences: SharedPreferences
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WordForTodayViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WordForTodayViewModel(repository, preferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

