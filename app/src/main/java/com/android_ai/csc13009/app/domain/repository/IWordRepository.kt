package com.android_ai.csc13009.app.domain.repository

import com.android_ai.csc13009.app.domain.models.WordModel

interface IWordRepository {
    suspend fun getWordByName(query: String): List<WordModel>
    suspend fun getWordById(id: Int): WordModel?
    suspend fun getSuggestions(prefix: String): List<WordModel>
    suspend fun getRandomWord(): WordModel?
}