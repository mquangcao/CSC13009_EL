package com.android_ai.csc13009.app.data.repository

import com.android_ai.csc13009.app.data.local.dao.GrammarLevelDao
import com.android_ai.csc13009.app.data.remote.repository.FirestoreGrammarLevelRepository
import com.android_ai.csc13009.app.domain.models.GrammarLevel
import com.android_ai.csc13009.app.domain.repository.IGrammarLevelRepository
import com.android_ai.csc13009.app.utils.mapper.toDomain

class GrammarLevelRepository(private val firestoreGrammarLevelRepository: FirestoreGrammarLevelRepository):
    IGrammarLevelRepository {
    override suspend fun getAllLevels(): List<GrammarLevel> {
        val firestoreLevels = firestoreGrammarLevelRepository.getAllLevels()
        return firestoreLevels.map { it.toDomain() }
    }

    override suspend fun getLevelByName(levelName: String): GrammarLevel? {
        val firestoreLevel = firestoreGrammarLevelRepository.getLevelByName(levelName)
        return firestoreLevel?.toDomain()
    }

}