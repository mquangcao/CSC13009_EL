package com.android_ai.csc13009.app.utils.mapper
import com.android_ai.csc13009.app.data.remote.model.FirestoreGrammarSubtopic
import com.android_ai.csc13009.app.domain.models.GrammarSubtopic

fun FirestoreGrammarSubtopic.toDomain(): GrammarSubtopic {
    return GrammarSubtopic(
        id = this.id,
        topicId = this.topicId,
        name = this.name,
        content = this.content,
        structures = this.structures,
        examples = this.examples
    )
}