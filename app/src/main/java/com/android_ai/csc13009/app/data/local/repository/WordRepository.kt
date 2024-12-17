package com.android_ai.csc13009.app.data.local.repository

import com.android_ai.csc13009.app.data.local.dao.WordDao
import com.android_ai.csc13009.app.domain.repository.model.Word
import com.android_ai.csc13009.app.domain.repository.repository.IWordRepository
import com.android_ai.csc13009.app.utils.mapper.toDomain

class WordRepository(private val wordDao: WordDao) : IWordRepository {

    override suspend fun getWordByName(query: String): List<Word> {
        return wordDao.getWordByName("%$query%").map { it.toDomain() }
    }

    override suspend fun getWordById(id: Int): Word? {
        return wordDao.getWordById(id)?.toDomain()
    }

    override suspend fun getSuggestions(prefix: String): List<Word> {
        return wordDao.getSuggestions(prefix).map { it.toDomain() }
    }
}