package com.android_ai.csc13009.app.data.local.repository

import com.android_ai.csc13009.app.data.local.dao.GrammarLevelDao
import com.android_ai.csc13009.app.domain.models.GrammarLevel
import com.android_ai.csc13009.app.domain.repository.IGrammarLevelRepository
import com.android_ai.csc13009.app.utils.mapper.toDomain

class GrammarLevelRepository(private val grammarLevelDao: GrammarLevelDao):
    IGrammarLevelRepository {
    override suspend fun getAllLevels(): List<GrammarLevel> {
        return grammarLevelDao.getAllLevels().map { it.toDomain() }
    }

}