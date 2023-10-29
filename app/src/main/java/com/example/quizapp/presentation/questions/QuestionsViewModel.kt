package com.example.quizapp.presentation.questions

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.domain.repository.QuestionsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class QuestionsViewModel @Inject constructor(
    private val questionsRepository: QuestionsRepository
) : ViewModel() {

    private val _currentQuestionState = mutableStateOf(CurrentQuestionState())
    val currentQuestionState: State<CurrentQuestionState> = _currentQuestionState

    private val _questionsListState = mutableStateOf(QuestionsListState())
    val questionsListState: State<QuestionsListState> = _questionsListState

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val questions = questionsRepository.getQuestions()
                _questionsListState.value = questionsListState.value.copy(questions.toMutableList())
                _currentQuestionState.value = currentQuestionState.value.copy(
                    questionNumber = 1,
                    questionsQuantity = questions.size,
                    question = questions[0],
                    isLoading = false
                )
                Log.d("TAG", questionsListState.value.toString())
                Log.d("TAG", _currentQuestionState.value.toString())
            }
        }
    }

    fun onEvent(event: QuestionEvent) {
        when (event) {
            is QuestionEvent.PreviousQuestion -> {
                _currentQuestionState.value = currentQuestionState.value.copy(
                    question = _questionsListState.value.list[_currentQuestionState.value.questionIndex - 1],
                    questionNumber = _currentQuestionState.value.questionNumber - 1,
                    questionIndex = _currentQuestionState.value.questionIndex - 1
                )
            }

            is QuestionEvent.NextQuestion -> {
                _currentQuestionState.value = currentQuestionState.value.copy(
                    question = _questionsListState.value.list[_currentQuestionState.value.questionIndex + 1],
                    questionNumber = _currentQuestionState.value.questionNumber + 1,
                    questionIndex = _currentQuestionState.value.questionIndex + 1
                )
            }

            is QuestionEvent.SubmitAnswer -> {
                _currentQuestionState.value = currentQuestionState.value.copy(
                    isLoading = true
                )
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        questionsRepository.checkAnswer(event.id, event.answer)
                        _questionsListState.value.list[currentQuestionState.value.questionIndex].isSubmitted = true
                        _currentQuestionState.value = currentQuestionState.value.copy(
                            isLoading = false
                        )
                    }
                }
            }

        }
    }

}