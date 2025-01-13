package com.android_ai.csc13009.app.utils.mapper

import com.android_ai.csc13009.app.data.local.entity.GrammarQuestionEntity
import com.android_ai.csc13009.app.domain.models.GrammarQuestion

fun GrammarQuestionEntity.toDomain(): GrammarQuestion{
    return GrammarQuestion(
        grammarQuestionId = this.grammarQuestionId,
        grammarTopicId = this.grammarTopicId,
        name = this.name,
        type = this.type
    )
}