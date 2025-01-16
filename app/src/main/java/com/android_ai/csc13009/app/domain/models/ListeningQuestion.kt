package com.android_ai.csc13009.app.domain.models

class ListeningQuestion (
    var id : String,
    var question : String,
    var type : String,
    var answer : ArrayList<ListeningAnswer>,
)