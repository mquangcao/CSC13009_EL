package com.android_ai.csc13009.app.utils.mapper
import com.android_ai.csc13009.app.data.remote.model.FirestoreGrammarTopic
import com.android_ai.csc13009.app.domain.models.GrammarTopic

fun FirestoreGrammarTopic.toDomain(): GrammarTopic {
    return GrammarTopic(
        id = this.id,
        levelId = this.levelId,
        name = this.name,
    )
}