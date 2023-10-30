package com.example.quizapp.presentation.questions

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.domain.model.AnswerStatus
import com.example.quizapp.domain.model.Question
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

    private var questions: MutableList<Question> = mutableListOf()

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                questions = questionsRepository.getQuestions().toMutableList()
                _currentQuestionState.value = currentQuestionState.value.copy(
                    questionNumber = 1,
                    questionsQuantity = questions.size,
                    question = questions[0],
                    isLoading = false
                )
                Log.d("TAG", questions.toString())
                Log.d("TAG", _currentQuestionState.value.toString())
            }
        }
    }

    fun onEvent(event: QuestionEvent) {
        when (event) {
            is QuestionEvent.PreviousQuestion -> {
                if (currentQuestion.isSubmitted) {
                    currentQuestion.isEnabled = false
                }
                currentQuestion.answerStatus = AnswerStatus.NOT_SET
                _currentQuestionState.value = currentQuestionState.value.copy(
                    question = questions[_currentQuestionState.value.questionIndex - 1],
                    questionNumber = _currentQuestionState.value.questionNumber - 1,
                    questionIndex = _currentQuestionState.value.questionIndex - 1
                )
            }

            is QuestionEvent.NextQuestion -> {
                if (currentQuestion.isSubmitted) {
                    currentQuestion.isEnabled = false
                }
                currentQuestion.answerStatus = AnswerStatus.NOT_SET
                _currentQuestionState.value = currentQuestionState.value.copy(
                    question = questions[_currentQuestionState.value.questionIndex + 1],
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
                        val result = questionsRepository.checkAnswer(event.id, event.answer)
                        if (result.requestSucceeded()){
                            currentQuestion.isSubmitted = true
                        }
                        currentQuestion.answerStatus = result
                        _currentQuestionState.value = currentQuestionState.value.copy(
                            isLoading = false,
                            questionsSubmitted =
                                if (result.requestSucceeded()) {
                                    currentQuestionState.value.questionsSubmitted + 1
                                } else {
                                    currentQuestionState.value.questionsSubmitted
                                }
                        )
                    }
                }
            }

        }
    }

    private val currentQuestion: Question
    get() {
        val index = currentQuestionState.value.questionIndex
        return questions[index]
    }

}