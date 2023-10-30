package com.example.quizapp.presentation.questions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.quizapp.domain.model.AnswerStatus

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
                        .padding(top = 100.dp, start = 8.dp, end = 8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.LightGray)
                            .padding(8.dp)
                            .height(40.dp)
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
                        if (currentQuestionState.question?.answerStatus!!.requestSucceeded()) {
                            VerdictBox(state = currentQuestionState, vm = viewModel)
                        } else {
                            Text(
                                text = currentQuestionState.question?.text ?: "No items found",
                                style = TextStyle(fontSize = 18.sp),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(top = 20.dp)
                                    .height(40.dp)
                                    .fillMaxWidth()
                                    .wrapContentHeight(Alignment.Top)
                            )
                        }
                        if (currentQuestionState.question?.isEnabled == true || currentQuestionState.question?.isSubmitted == false) {
                            TextField(
                                value = answer,
                                onValueChange = { answer = it },
                                label = { Text("Type here for an answer...") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                            )
                        }
                    }
                    val question = currentQuestionState.question
                    if (question != null) {
                        Button(
                            enabled = question.isSubmitted.not() && answer.isEmpty().not(),
                            onClick = {
                                viewModel.onEvent(
                                    QuestionEvent.SubmitAnswer(
                                        question.id,
                                        answer
                                    )
                                )
                            }, modifier = Modifier
                                .padding(20.dp)
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

    var snackbarShown by rememberSaveable { mutableStateOf(false) }

    if (currentQuestionState.question?.answerStatus != AnswerStatus.NOT_SET && snackbarShown.not()) {
        val snackbarMessage = when (currentQuestionState.question?.answerStatus) {
            AnswerStatus.CORRECT -> "Success!"
            AnswerStatus.WRONG -> "Failure..."
            AnswerStatus.ERROR -> "Something went wrong"
            else -> "" // never occurs
        }
        LaunchedEffect(snackbarMessage) {
            snackbarHostState.showSnackbar(
                message = snackbarMessage, duration = SnackbarDuration.Short
            )
            snackbarShown = true
        }
    }
}

@Composable
fun VerdictBox(state: CurrentQuestionState, vm: QuestionsViewModel) {
    val verdict = state.question!!.answerStatus
    val color = if (verdict == AnswerStatus.CORRECT) Color.Green else Color.Red
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color)
                .padding(8.dp)
                .wrapContentWidth(Alignment.Start)
        ) {
            if (verdict == AnswerStatus.CORRECT) {
                Text(
                    text = "Success",
                    color = Color.Black,
                    style = TextStyle(fontSize = 18.sp)
                )
            } else {
                Text(
                    text = "Failure!",
                    color = Color.Black,
                    style = TextStyle(fontSize = 18.sp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            if (verdict == AnswerStatus.WRONG) {
                Button(
                    onClick = { vm.onEvent(QuestionEvent.Retry) },
                ) {
                    Text(
                        text = "Retry",
                        color = Color.White,
                        style = TextStyle(fontSize = 18.sp)
                    )
                }
            }
        }
    }
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