package com.android_ai.csc13009.app.utils.mapper

import com.android_ai.csc13009.app.data.remote.model.FirestoreGrammarQuestion
import com.android_ai.csc13009.app.domain.models.GrammarQuestion

fun FirestoreGrammarQuestion.toDomain(): GrammarQuestion {
    return GrammarQuestion(
        id = this.id,
        grammarTopicId = this.grammarTopicId,
        name = this.name,
        type = this.type
    )
}