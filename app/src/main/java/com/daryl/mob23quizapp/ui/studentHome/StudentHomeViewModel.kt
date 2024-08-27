package com.daryl.mob23quizapp.ui.studentHome

import androidx.lifecycle.viewModelScope
import com.daryl.mob23quizapp.R
import com.daryl.mob23quizapp.core.services.AuthService
import com.daryl.mob23quizapp.core.utils.ResourceProvider
import com.daryl.mob23quizapp.core.utils.Utils.debugLog
import com.daryl.mob23quizapp.data.models.Participant
import com.daryl.mob23quizapp.data.models.Question
import com.daryl.mob23quizapp.data.models.Quiz
import com.daryl.mob23quizapp.data.repositories.QuizRepo
import com.daryl.mob23quizapp.data.repositories.UserRepo
import com.daryl.mob23quizapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentHomeViewModel @Inject constructor(
    private val authService: AuthService,
    private val quizRepo: QuizRepo,
    private val userRepo: UserRepo,
    private val resourceProvider: ResourceProvider
): BaseViewModel() {
    private val _quiz = MutableSharedFlow<Quiz?>()
    val quiz: SharedFlow<Quiz?> = _quiz
    private val _result = MutableSharedFlow<Triple<Int, Int, Int>?>()
    val result: SharedFlow<Triple<Int, Int, Int>?> = _result
    private var currentQuiz: Quiz? = null
    fun getQuizById(quizId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            globalErrorHandler {
                delay(500)
                if(quizId.isEmpty()) throw Exception("Please enter a valid Quiz ID.")
                currentQuiz = quizRepo.getQuizById(quizId) ?: throw Exception("Quiz doesn't exist.")
                _quiz.emit(currentQuiz as Quiz)
            }
        }
    }
    fun updateParticipantList(quizId: String, score: Int, timeTaken: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            globalErrorHandler {
                delay(500)
                val quiz = quizRepo.getQuizById(quizId) ?: throw Exception("Error updating ranking.")
                val user = userRepo.getUserById() ?: throw Exception("User doesn't exist.")
                val list = quiz.participants.toMutableList()
                val participant =
                    Participant(user.email, score, timeTaken)
                val idx = list.indexOfFirst { it.studentEmail == user.email }
                if(idx != -1) list[idx] = participant
                else list.add(participant)
                quizRepo.updateQuiz(quiz.copy(participants = list))
                _result.emit(Triple(quiz.questions.size, score, timeTaken))
            }?.let {
                _submit.emit(resourceProvider.getString(R.string.score_update))
            }
        }
    }
    fun getScore(answerList: List<String>): Int {
        val results = mutableListOf<Boolean>()
        val questions = currentQuiz!!.questions
        questions.forEachIndexed { index, _ ->
            results.add(questions[index].answer == answerList[index])
        }
        return results.count { it }
    }

    fun getCurrentQuiz(): Quiz? = currentQuiz
}