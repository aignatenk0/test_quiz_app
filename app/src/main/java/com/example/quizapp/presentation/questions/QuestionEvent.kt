package com.example.quizapp.presentation.questions

sealed class QuestionEvent {
    object PreviousQuestion: QuestionEvent()

    object NextQuestion: QuestionEvent()

    data class SubmitAnswer(val id: Int, val answer: String): QuestionEvent()
}