package com.android_ai.csc13009.app.utils.mapper

import com.android_ai.csc13009.app.data.local.entity.GrammarLevelEntity
import com.android_ai.csc13009.app.domain.repository.model.GrammarLevel

fun GrammarLevelEntity.toDomain(): GrammarLevel {
    return GrammarLevel(
        id = this.id,
        name = this.name
    )
}