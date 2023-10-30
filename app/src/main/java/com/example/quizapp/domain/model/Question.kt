package com.example.quizapp.domain.model

data class Question(
    val id: Int,
    val text: String,
    var isEnabled: Boolean = true,
    var isSubmitted: Boolean = false,
    var answerStatus: AnswerStatus = AnswerStatus.NOT_SET
)