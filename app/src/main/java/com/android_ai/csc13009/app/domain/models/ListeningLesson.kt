package com.android_ai.csc13009.app.domain.models

class ListeningLesson (
    var id: String,
    var lessonName: String,
    var totalQuestion : Int,
    var questionSuccess : Int,
    var order : Int,
    var questions : List<ListeningQuestion>,
    var isOpen : Boolean,
    var isOpenByProgress : Boolean
)