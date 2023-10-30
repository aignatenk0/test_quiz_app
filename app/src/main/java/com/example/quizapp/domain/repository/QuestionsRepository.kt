package com.example.quizapp.domain.repository

import com.example.quizapp.domain.model.Question
import com.example.quizapp.domain.model.AnswerStatus

interface QuestionsRepository {

    suspend fun getQuestions(): List<Question>

    suspend fun checkAnswer(id: Int, answer: String): AnswerStatus
}