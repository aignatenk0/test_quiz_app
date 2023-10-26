package com.example.quizapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quizapp.presentation.main.MainScreen
import com.example.quizapp.presentation.questions.QuestionScreen

@Composable
fun QuizNavHost() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.MainScreen.route
    ) {
        composable(
            route = Screen.MainScreen.route
        ) {
            MainScreen(navHostController = navController)
        }
        composable(route = Screen.QuestionsScreen.route) {
            QuestionScreen(navHostController = navController)
        }
    }
}