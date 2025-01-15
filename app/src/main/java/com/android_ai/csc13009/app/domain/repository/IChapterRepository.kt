package com.android_ai.csc13009.app.domain.repository

import com.android_ai.csc13009.app.domain.models.Chapter
import com.android_ai.csc13009.app.domain.models.Chapters

interface IChapterRepository {
    suspend fun getChapterList(): List<Chapters>
    suspend fun getChapterDetail(chapterId: String): Chapter
}