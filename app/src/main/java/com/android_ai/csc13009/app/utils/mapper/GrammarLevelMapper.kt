package com.android_ai.csc13009.app.utils.mapper
import com.android_ai.csc13009.app.data.remote.model.FirestoreGrammarLevel
import com.android_ai.csc13009.app.domain.models.GrammarLevel

fun FirestoreGrammarLevel.toDomain(): GrammarLevel {
    return GrammarLevel(
        id = this.id,
        name = this.name
    )
}