package com.android_ai.csc13009.app.data.remote.model

import com.google.firebase.firestore.DocumentId

data class FirestoreGrammarLevel(
    @DocumentId val id: String? = "",
    val name: String = ""
)