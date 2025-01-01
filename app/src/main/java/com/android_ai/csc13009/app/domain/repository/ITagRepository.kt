package com.android_ai.csc13009.app.domain.repository

import com.android_ai.csc13009.app.domain.models.Tag

interface ITagRepository {
    suspend fun createTag(userId: String, tagName: String) : String? // return TagID
    suspend fun addWordToTag(tagId: String, wordId: Int)
    suspend fun getUserTags(userId: String): List<Tag>
    suspend fun deleteTag(tagId: String)
    suspend fun getTagById(tagId: String): Tag?
}