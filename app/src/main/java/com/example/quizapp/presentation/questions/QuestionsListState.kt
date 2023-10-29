package com.example.quizapp.presentation.questions

import com.example.quizapp.domain.model.Question

data class QuestionsListState(
    val list: MutableList<Question> = mutableListOf()
)
