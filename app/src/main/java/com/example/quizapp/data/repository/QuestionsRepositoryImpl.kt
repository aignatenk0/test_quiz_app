package com.example.quizapp.data.repository

import com.example.quizapp.data.datasource.remote.ApiService
import com.example.quizapp.data.datasource.remote.dto.AnswerDto
import com.example.quizapp.domain.model.Question
import com.example.quizapp.domain.repository.QuestionsRepository

class QuestionsRepositoryImpl(
    private val apiService: ApiService
) : QuestionsRepository {
    override suspend fun getQuestions(): List<Question> {
        try {
            val resp = apiService.getQuestions()
            if (resp.isSuccessful.not() || resp.body() == null) {
                return emptyList()
            }
            return resp.body()!!.map { dto->
                Question(
                    dto.id,
                    dto.question
                )
            }
        } catch (e: Exception) {
            return emptyList()
        }
    }

    override suspend fun checkAnswer(id: Int, answer: String): Boolean {
        val resp = apiService.checkAnswer(
            AnswerDto(id, answer)
        )
        return false
    }
}