package com.android_ai.csc13009.app.domain.repository

import com.android_ai.csc13009.app.domain.models.WordPhonetic

interface IWordPhoneticRepository {
    fun getWordPhonetics(
        word: String,
        onSuccess: (List<WordPhonetic>) -> Unit,
        onError: (String) -> Unit
    )
}