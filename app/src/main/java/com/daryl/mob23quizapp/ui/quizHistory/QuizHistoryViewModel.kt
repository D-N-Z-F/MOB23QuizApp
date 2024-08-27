package com.daryl.mob23quizapp.ui.quizHistory

import com.daryl.mob23quizapp.data.models.Quiz
import com.daryl.mob23quizapp.data.repositories.QuizRepo
import com.daryl.mob23quizapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class QuizHistoryViewModel @Inject constructor(
    private val quizRepo: QuizRepo
): BaseViewModel() {
    fun getStudentQuizHistory(): Flow<List<Quiz>> = quizRepo.getStudentQuizHistory()
}