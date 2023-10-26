package com.example.quizapp.presentation.navigation

sealed class Screen(val route: String) {
    object MainScreen : Screen(route = "main_screen")
    object QuestionsScreen : Screen(route = "questions_screen")
}
