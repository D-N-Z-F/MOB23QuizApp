package com.daryl.mob23quizapp.ui.viewQuiz

import androidx.lifecycle.viewModelScope
import com.daryl.mob23quizapp.data.models.Quiz
import com.daryl.mob23quizapp.data.repositories.QuizRepo
import com.daryl.mob23quizapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewQuizViewModel @Inject constructor(
    private val quizRepo: QuizRepo
): BaseViewModel() {
    private val _quiz = MutableSharedFlow<Quiz>()
    val quiz: SharedFlow<Quiz> = _quiz
    fun getQuizById(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            globalErrorHandler {
                val quiz = quizRepo.getQuizById(id) ?: throw Exception("Quiz doesn't exist!")
                _quiz.emit(quiz)
            }
        }
    }
}