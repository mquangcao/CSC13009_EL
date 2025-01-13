package com.android_ai.csc13009.app.utils.mapper

import com.android_ai.csc13009.app.data.local.entity.GrammarTopicEntity
import com.android_ai.csc13009.app.domain.models.GrammarTopic

fun GrammarTopicEntity.toDomain(): GrammarTopic {
    return GrammarTopic(
        id = this.id,
        levelId = this.levelId,
        name = this.name
    )
}