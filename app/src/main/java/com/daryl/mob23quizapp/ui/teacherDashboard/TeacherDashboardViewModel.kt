package com.daryl.mob23quizapp.ui.teacherDashboard

import androidx.lifecycle.viewModelScope
import com.daryl.mob23quizapp.R
import com.daryl.mob23quizapp.core.Constants.DELETE
import com.daryl.mob23quizapp.core.utils.ResourceProvider
import com.daryl.mob23quizapp.data.models.Quiz
import com.daryl.mob23quizapp.data.repositories.QuizRepo
import com.daryl.mob23quizapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeacherDashboardViewModel @Inject constructor(
    private val quizRepo: QuizRepo,
    private val resourceProvider: ResourceProvider
): BaseViewModel() {
    fun getTeacherQuizzes(): Flow<List<Quiz>> = quizRepo.getTeacherQuizzes()
    fun deleteQuiz(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            globalErrorHandler {
                quizRepo.deleteQuiz(id)
            }?.let {
                _submit.emit(
                    resourceProvider.getString(R.string.success_message, DELETE)
                )
            }
        }
    }
}