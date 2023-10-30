package com.example.quizapp.presentation.questions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionScreen(
    viewModel: QuestionsViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val currentQuestionState by viewModel.currentQuestionState

    Scaffold(
        topBar = { TopBar(currentQuestionState, navHostController, viewModel) },
        content = { padding ->
            if (currentQuestionState.isLoading.not()) {
                var answer by rememberSaveable { mutableStateOf("") }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 90.dp, start = 8.dp, end = 8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.LightGray)
                            .padding(8.dp)
                            .height(44.dp)
                    ) {
                        Text(
                            text = "Questions submitted: ${currentQuestionState.questionsSubmitted}",
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = currentQuestionState.question?.text ?: "No items found",
                            style = TextStyle(fontSize = 24.sp),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(top = 40.dp)
                                .height(60.dp)
                                .fillMaxWidth()
                                .wrapContentHeight(Alignment.Top) // Align Text to the top
                        )
                        TextField(
                            value = answer,
                            onValueChange = { answer = it },
                            label = { Text("Type here for an answer...") },
                            modifier = Modifier
                                .fillMaxWidth()
                            )
                    }
                    val question = currentQuestionState.question
                    if (question != null) {
                        Button(
                            enabled = question.isSubmitted.not() && answer.isEmpty().not(),
                            onClick = {
                                viewModel.onEvent(
                                    QuestionEvent.SubmitAnswer(
                                        question.id,
                                        "Text"
                                    )
                                )
                            }, modifier = Modifier
                                .padding(padding)
                                .align(Alignment.CenterHorizontally)
                        ) {
                            if (question.isSubmitted) {
                                Text("Already submitted")
                            } else {
                                Text("Submit")
                            }
                        }
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(450.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        color = Color.Blue
                    )
                }
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(10.dp),
            )
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    state: CurrentQuestionState,
    navHostController: NavHostController,
    vm: QuestionsViewModel
) {
    TopAppBar(navigationIcon = {
        IconButton(onClick = { navHostController.popBackStack() }) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
        }
    }, title = {
        Text(
            text = "Question ${state.questionNumber}/${state.questionsQuantity}",
            style = TextStyle(
                color = Color.Unspecified, fontSize = 20.sp
            )
        )
    }, actions = {
        TextButton(
            onClick = { vm.onEvent(QuestionEvent.PreviousQuestion) },
            enabled = state.isLoading.not() && state.questionNumber > 1
        ) {
            Text(text = "Previous")
        }
        TextButton(
            onClick = { vm.onEvent(QuestionEvent.NextQuestion) },
            enabled = state.isLoading.not() && state.questionNumber < state.questionsQuantity
        ) {
            Text(text = "Next")
        }
    })
}