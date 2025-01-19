package com.android_ai.csc13009.app.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android_ai.csc13009.app.data.remote.model.TagState
import com.android_ai.csc13009.app.data.repository.TagRepository
import com.android_ai.csc13009.app.utils.mapper.TagMapper
import kotlinx.coroutines.launch

class TagViewModel(
    private val tagRepository: TagRepository
): ViewModel() {

    private val _tagState = MutableLiveData<TagState>()
    val tagState: LiveData<TagState> get() = _tagState

    fun createTag(userId: String, tagName: String) {
        viewModelScope.launch {
            try {
                val tagId = tagRepository.createTag(userId, tagName)
                if (tagId != null) {
                    _tagState.postValue(TagState.Success("Tag created successfully!"))
                } else {
                    _tagState.postValue(TagState.Error("Failed to create tag."))
                }
            } catch (e: Exception) {
                _tagState.postValue(TagState.Error("Error creating tag: ${e.message}"))
            }
        }
    }

    fun addWordToTag(tagId: String, wordId: Int) {
        viewModelScope.launch {
            try {
                tagRepository.addWordToTag(tagId, wordId)
                _tagState.postValue(TagState.Success("Word added successfully to tag."))
            } catch (e: Exception) {
                _tagState.postValue(TagState.Error("Error adding word to tag: ${e.message}"))
            }
        }
    }

    fun getUserTags(userId: String) {
        viewModelScope.launch {
            try {
                val tags = tagRepository.getUserTags(userId)
                _tagState.postValue(TagState.TagList(tags))
            } catch (e: Exception) {
                _tagState.postValue(TagState.Error("Error fetching tags: ${e.message}"))
            }
        }
    }

    fun deleteTag(tagId: String) {
        viewModelScope.launch {
            try {
                tagRepository.deleteTag(tagId)
                _tagState.postValue(TagState.Success("Tag deleted successfully."))
            } catch (e: Exception) {
                _tagState.postValue(TagState.Error("Error deleting tag: ${e.message}"))
            }
        }
    }

    fun deleteWordFromTag(wordId: Int, tagId: String) {
        viewModelScope.launch {
            try {
                tagRepository.deleteWordFromTag(wordId, tagId)
                _tagState.postValue(TagState.Success("Word deleted successfully from tag."))
            } catch (e: Exception) {
                _tagState.postValue(TagState.Error("Error deleting word from tag: ${e.message}"))
            }
        }
    }
}