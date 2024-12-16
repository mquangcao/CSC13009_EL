package com.android_ai.csc13009.app.domain.repository.repository
import com.android_ai.csc13009.app.domain.repository.model.GrammarLevel

interface IGrammarLevelRepository {
    suspend fun getAllLevels(): List<GrammarLevel>
}