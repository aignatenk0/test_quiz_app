package com.example.quizapp.domain.model

enum class AnswerStatus {
    NOT_SET,
    CORRECT,
    WRONG,
    ERROR;

    fun requestSucceeded(): Boolean {
        return this == CORRECT || this == WRONG
    }
}