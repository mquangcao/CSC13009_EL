package com.android_ai.csc13009.app.data.repository

import com.android_ai.csc13009.app.data.remote.repository.FirestoreTagRepository
import com.android_ai.csc13009.app.domain.models.Tag
import com.android_ai.csc13009.app.domain.repository.ITagRepository

class TagRepository(
    private val firestoreTagRepository: FirestoreTagRepository
): ITagRepository {
    override suspend fun createTag(userId: String, tagName: String): String? {
        return firestoreTagRepository.createTag(userId, tagName)
    }

    override suspend fun addWordToTag(tagId: String, wordId: Int) {
        firestoreTagRepository.addWordToTag(tagId, wordId)
    }

    override suspend fun getUserTags(userId: String): List<Tag> {
        return firestoreTagRepository.getUserTags(userId)
    }

    override suspend fun deleteTag(tagId: String) {
        firestoreTagRepository.deleteTag(tagId)
    }

    override suspend fun getTagById(tagId: String): Tag? {
        return firestoreTagRepository.getTagById(tagId)
    }

    override suspend fun deleteWordFromTag(wordId: Int, tagId: String) {
        firestoreTagRepository.deleteWordFromTag(wordId, tagId)
    }

    override suspend fun updateTagName(tagId: String, newName: String) {
        firestoreTagRepository.updateTagName(tagId, newName)
    }
}