package com.android_ai.csc13009.app.presentation.model

data class Language(
    val id: Int,
    val name: String,
    val code : String,
    var isSelected: Boolean = false
)