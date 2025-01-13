package com.android_ai.csc13009.app.utils.mapper

import com.android_ai.csc13009.app.data.local.entity.WordEntity
import com.android_ai.csc13009.app.domain.models.WordModel

fun WordEntity.toDomain(): WordModel {
    return WordModel(
        id = this.id,
        word = this.word,
        pronunciation = this.pronunciation,
        details = this.details
    )
}