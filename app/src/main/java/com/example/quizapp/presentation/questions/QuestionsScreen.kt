package com.example.quizapp.presentation.questions

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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
    viewModel: QuestionsViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    val coroutineScope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {TopBar(navHostController, viewModel)},
        content = { padding ->
            val currentItem = viewModel.itemList.getOrNull(viewModel.currentItemIndex)
            Text(
                text = currentItem ?: "No item found",
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
                    .padding(16.dp),
                style = TextStyle(fontSize = 24.sp),
                textAlign = TextAlign.Center
            )

            Button(
                onClick = {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = "Snackbar Message",
                            duration = SnackbarDuration.Short
                        )
                    }
                },
                modifier = Modifier.padding(padding)
            ) {
                Text("Show Snackbar")
            }
        },

        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(16.dp),
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navHostController: NavHostController, vm: QuestionsViewModel) {
    TopAppBar(
        navigationIcon = {
            IconButton(
                onClick = { navHostController.popBackStack() }
            ) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        },
        title = {
            Text(
                text = "Title",
                style = TextStyle(
                    color = Color.Unspecified,
                    fontSize = 24.sp
                )
            )
        },
        actions = {
            Button(onClick = { vm.currentItemIndex-- }) {

                Text(text = "Previous")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { vm.currentItemIndex++ }) {

                Text(text = "Next")
            }
        }
    )
}