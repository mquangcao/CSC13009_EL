package com.android_ai.csc13009.app.data.remote.model

data class WordPhoneticResponse(
    val word: String,
    val phonetics: List<Phonetic>
) {
    data class Phonetic(
        val audio: String? // Audio URL
    )
}