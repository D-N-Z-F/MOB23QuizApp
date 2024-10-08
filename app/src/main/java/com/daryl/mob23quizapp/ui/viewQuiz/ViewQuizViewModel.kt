package com.daryl.mob23quizapp.ui.viewQuiz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.daryl.mob23quizapp.R
import com.daryl.mob23quizapp.core.Constants.ARG_NAME
import com.daryl.mob23quizapp.core.Constants.EDIT
import com.daryl.mob23quizapp.core.Constants.NON_EXISTENT_QUIZ
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
    init { savedStateHandle.get<String>(ARG_NAME)?.let { getQuizById(it) } }
    fun getAllStudents(): Flow<List<User>> = userRepo.getAllStudents()
    private fun getQuizById(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            globalErrorHandler {
                quizRepo.getQuizById(id)?.let {
                    _quiz.emit(it)
                } ?: throw Exception(NON_EXISTENT_QUIZ)
            }
        }
    }
    fun updateQuiz(quiz: Quiz) {
        viewModelScope.launch {
            globalErrorHandler {
                quizRepo.updateQuiz(quiz)
                quiz.id?.let {
                    getQuizById(it)
                    _submit.emit(
                        resourceProvider.getString(
                            R.string.success_message, EDIT.capitalize()
                        )
                    )
                } ?: throw Exception(NON_EXISTENT_QUIZ)
            }
        }
    }
}