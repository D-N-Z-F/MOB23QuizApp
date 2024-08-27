package com.daryl.mob23quizapp.ui.viewQuiz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.daryl.mob23quizapp.R
import com.daryl.mob23quizapp.core.Constants.EDIT
import com.daryl.mob23quizapp.core.utils.ResourceProvider
import com.daryl.mob23quizapp.core.utils.Utils.capitalize
import com.daryl.mob23quizapp.data.models.Quiz
import com.daryl.mob23quizapp.data.models.User
import com.daryl.mob23quizapp.data.repositories.QuizRepo
import com.daryl.mob23quizapp.data.repositories.UserRepo
import com.daryl.mob23quizapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewQuizViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val quizRepo: QuizRepo,
    private val userRepo: UserRepo,
    private val resourceProvider: ResourceProvider
): BaseViewModel() {
    private val _quiz = MutableSharedFlow<Quiz>()
    val quiz: SharedFlow<Quiz> = _quiz
    init {
        savedStateHandle.get<String>("quizId")?.let { getQuizById(it) }
    }
    fun getAllStudents(): Flow<List<User>> = userRepo.getAllStudents()
    private fun getQuizById(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            globalErrorHandler {
                val quiz = quizRepo.getQuizById(id) ?: throw Exception("Quiz doesn't exist.")
                _quiz.emit(quiz)
            }
        }
    }
    fun updateQuiz(quiz: Quiz) {
        viewModelScope.launch {
            globalErrorHandler {
                quizRepo.updateQuiz(quiz)
                getQuizById(quiz.id!!)
            }?.let {
                _submit.emit(
                    resourceProvider.getString(R.string.success_message, EDIT.capitalize())
                )
            }
        }
    }
}