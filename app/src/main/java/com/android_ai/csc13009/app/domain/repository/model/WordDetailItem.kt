package com.android_ai.csc13009.app.domain.repository.model

data class WordDetailItem(
    val type: String, // Loại: '-', '=', '+', '*', ''
    val content: String // Nội dung tương ứng
)