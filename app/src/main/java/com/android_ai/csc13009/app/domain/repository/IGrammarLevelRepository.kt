package com.android_ai.csc13009.app.domain.repository
import com.android_ai.csc13009.app.domain.models.GrammarLevel

interface IGrammarLevelRepository {
    suspend fun getAllLevels(): List<GrammarLevel>
}