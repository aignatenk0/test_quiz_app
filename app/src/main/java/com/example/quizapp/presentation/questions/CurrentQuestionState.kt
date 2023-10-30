package com.example.quizapp.presentation.questions

import com.example.quizapp.domain.model.Question

data class CurrentQuestionState(
    val questionNumber: Int = 0,
    val questionIndex: Int = 0,
    val questionsQuantity: Int = 0,
    val questionsSubmitted: Int = 0,
    val isLoading: Boolean = true,
    val question: Question? = Question(
        id = -1,
        text = ""
    )
)
