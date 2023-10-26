package com.example.quizapp.presentation.questions

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuestionsViewModel @Inject constructor(): ViewModel() {

    val itemList = listOf("one", "two", "three")
    var currentItemIndex = 0
}