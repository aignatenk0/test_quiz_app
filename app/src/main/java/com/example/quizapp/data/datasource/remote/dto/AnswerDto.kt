package com.example.quizapp.data.datasource.remote.dto

import com.google.gson.annotations.SerializedName

data class AnswerDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("answer")
    val answer: String
)
