package com.android_ai.csc13009.app.domain.models

data class Level(
    val id: String,
    val text: String,
    val icon: Int,
    var isSelected: Boolean = false
)