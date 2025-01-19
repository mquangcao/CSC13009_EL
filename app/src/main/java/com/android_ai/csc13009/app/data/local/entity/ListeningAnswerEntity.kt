package com.android_ai.csc13009.app.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "ListeningAnswer")
data class ListeningAnswerEntity(
    @PrimaryKey
    val id: String,
    val questionId: String,
    val text : String,
    val isCorrect: Boolean,
    val imgUrl: String
)
