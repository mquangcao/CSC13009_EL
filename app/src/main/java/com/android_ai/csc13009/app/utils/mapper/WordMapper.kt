package com.android_ai.csc13009.app.utils.mapper

import com.android_ai.csc13009.app.data.local.entity.WordEntity
import com.android_ai.csc13009.app.domain.repository.model.Word

fun WordEntity.toDomain(): Word {
    return Word(
        id = this.id,
        word = this.word,
        pronunciation = this.pronunciation,
        details = this.details
    )
}