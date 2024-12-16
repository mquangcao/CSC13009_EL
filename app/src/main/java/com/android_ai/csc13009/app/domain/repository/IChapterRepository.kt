package com.android_ai.csc13009.app.domain.repository

import com.android_ai.csc13009.app.domain.models.Chapters

interface IChapterRepository {
    fun getChapterList(): List<Chapters>
}