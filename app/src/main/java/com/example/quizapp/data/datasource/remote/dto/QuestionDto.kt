package com.example.quizapp.data.datasource.remote.dto

import com.google.gson.annotations.SerializedName

data class QuestionDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("question")
    val question: String
)