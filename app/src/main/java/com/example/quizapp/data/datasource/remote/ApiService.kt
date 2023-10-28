package com.example.quizapp.data.datasource.remote

import com.example.quizapp.data.datasource.remote.dto.AnswerDto
import com.example.quizapp.data.datasource.remote.dto.QuestionDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("questions")
    suspend fun getQuestions(): Response<List<QuestionDto>>

    @POST("question/submit")
    suspend fun checkAnswer(answer: AnswerDto): Response<Unit>

    companion object {
        const val BASE_URL = "https://xm-assignment.web.app/"
    }
}