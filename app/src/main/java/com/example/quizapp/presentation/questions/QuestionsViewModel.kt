package com.example.quizapp.presentation.questions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.domain.repository.QuestionsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionsViewModel @Inject constructor(
    private val questionsRepository: QuestionsRepository
): ViewModel() {

    val itemList = listOf("one", "two", "three")

    private val _currentItemIndex = MutableLiveData(0)
    val currentItemIndex: LiveData<Int> get() = _currentItemIndex

    fun update(increase: Boolean) {
        val currentValue = _currentItemIndex.value ?: 0
        _currentItemIndex.value = if (increase) currentValue + 1 else currentValue - 1
    }

    init {
        viewModelScope.launch {
            questionsRepository.getQuestions()
        }
    }

}