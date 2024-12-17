package com.android_ai.csc13009.app.domain.repository.repository

import com.android_ai.csc13009.app.domain.repository.model.Word

interface IWordRepository {
    suspend fun getWordByName(query: String): List<Word>
    suspend fun getWordById(id: Int): Word?
    suspend fun getSuggestions(prefix: String): List<Word>
    suspend fun getRandomWord(): Word?
}