package com.android_ai.csc13009.app.data.remote.model

import com.android_ai.csc13009.app.domain.models.Tag

sealed class TagState {
    data class Success(val message: String) : TagState()
    data class Error(val error: String) : TagState()
    data class TagList(val tags: List<Tag>) : TagState()
}
