package com.example.quizapp.presentation.questions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionScreen(
    viewModel: QuestionsViewModel = hiltViewModel(), navHostController: NavHostController
) {
    val coroutineScope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }

    val index by viewModel.currentItemIndex.observeAsState()

    Scaffold(
        topBar = { TopBar(index!!, navHostController, viewModel) },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
                    .padding(0.dp)
            ) {
                val currentItem = viewModel.itemList.getOrNull(index!!)
                Text(
                    text = currentItem ?: "No item found",
                    style = TextStyle(fontSize = 24.sp),
                    textAlign = TextAlign.Center
                )

                Button(
                    onClick = {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Snackbar Message", duration = SnackbarDuration.Short
                            )
                        }
                    }, modifier = Modifier.padding(padding)
                ) {
                    Text("Show Snackbar")
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
fun TopBar(index: Int, navHostController: NavHostController, vm: QuestionsViewModel) {
    val questionsQuantity = vm.itemList.size
    TopAppBar(navigationIcon = {
        IconButton(onClick = { navHostController.popBackStack() }) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
        }
    }, title = {
        Text(
            text = "Question ${index + 1}/$questionsQuantity", style = TextStyle(
                color = Color.Unspecified, fontSize = 20.sp
            )
        )
    }, actions = {
        TextButton(
            onClick = { vm.update(false) },
            enabled = index > 0
        ) {
            Text(text = "Previous")
        }
        TextButton(
            onClick = { vm.update(true) },
            enabled = index < questionsQuantity - 1
        ) {
            Text(text = "Next")
        }
    })
}