package com.android_ai.csc13009.app.domain.models

import java.io.Serializable

class AnswerWord : Serializable {
    var id: String = ""
    var questionId: String = ""
    var text: String = ""
    var imgUrl : String = ""
    var isCorrect: Boolean = false
    var isSelected: Boolean = false
}