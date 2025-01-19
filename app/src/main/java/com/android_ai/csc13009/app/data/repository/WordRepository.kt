package com.android_ai.csc13009.app.data.repository

import com.android_ai.csc13009.app.data.local.dao.WordDao
import com.android_ai.csc13009.app.domain.models.WordModel
import com.android_ai.csc13009.app.domain.repository.IWordRepository
import com.android_ai.csc13009.app.utils.mapper.toDomain

class WordRepository(private val wordDao: WordDao) : IWordRepository {

    override suspend fun getWordByName(query: String): List<WordModel> {
        return wordDao.getWordByName("%$query%").map { it.toDomain() }
    }

    override suspend fun getWordById(id: Int): WordModel? {
        return wordDao.getWordById(id)?.toDomain()
    }

    override suspend fun getSuggestions(prefix: String): List<WordModel> {
        return wordDao.getSuggestions(prefix).map { it.toDomain() }
    }

    override suspend fun getRandomWord(): WordModel? {
        val allWords = wordDao.getAllWords()
        return allWords.randomOrNull()?.toDomain()
    }

    override suspend fun getRandomWords(count: Int): List<WordModel> {
        val allWords = wordDao.getAllWords()
        return allWords.shuffled().take(count).map { it.toDomain() }
    }

    override suspend fun getRandomWords(count: Int, maxLength: Int): List<WordModel> {
        val allWords = wordDao.getAllWords(maxLength)
        return allWords.shuffled().take(count).map { it.toDomain() }
    }

    // giong getWordByName nhung lay 1 tu duy nhat
    suspend fun getExactWordByName(query: String): WordModel? {
        return wordDao.getExactWord(query)?.toDomain()
    }
    suspend fun getWordsByIds(wordIds: List<Int>): List<String> {
        return wordIds.mapNotNull { id ->
            getWordById(id)?.word // Lấy tên từ vựng theo ID
        }
    }

}