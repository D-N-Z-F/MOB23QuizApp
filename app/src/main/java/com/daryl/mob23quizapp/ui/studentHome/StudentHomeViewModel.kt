package com.daryl.mob23quizapp.ui.studentHome

import androidx.lifecycle.viewModelScope
import com.daryl.mob23quizapp.R
import com.daryl.mob23quizapp.core.Constants
import com.daryl.mob23quizapp.core.Constants.EMPTY_QUIZ_ID
import com.daryl.mob23quizapp.core.Constants.LOAD_DELAY_TIMING
import com.daryl.mob23quizapp.core.Constants.NON_EXISTENT_QUIZ
import com.daryl.mob23quizapp.core.Constants.NON_EXISTENT_USER
import com.daryl.mob23quizapp.core.utils.ResourceProvider
import com.daryl.mob23quizapp.data.models.Participant
import com.daryl.mob23quizapp.data.models.Quiz
import com.daryl.mob23quizapp.data.models.Results
import com.daryl.mob23quizapp.data.repositories.QuizRepo
import com.daryl.mob23quizapp.data.repositories.UserRepo
import com.daryl.mob23quizapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentHomeViewModel @Inject constructor(
    private val quizRepo: QuizRepo,
    private val userRepo: UserRepo,
    private val resourceProvider: ResourceProvider
): BaseViewModel() {
    private val _quiz = MutableSharedFlow<Quiz?>()
    val quiz: SharedFlow<Quiz?> = _quiz
    private val _result = MutableSharedFlow<Results?>()
    val result: SharedFlow<Results?> = _result
    private var quizId: String? = null
    fun getQuizById(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            globalErrorHandler {
                delay(LOAD_DELAY_TIMING)
                if(id.isEmpty()) throw Exception(EMPTY_QUIZ_ID)
                quizRepo.getQuizById(id)?.let {
                    quizId = it.id
                    _quiz.emit(it)
                } ?: throw Exception(NON_EXISTENT_QUIZ)
            }
        }
    }
    fun updateQuiz(id: String, score: Int, timeTaken: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            globalErrorHandler {
                delay(LOAD_DELAY_TIMING)
                val email = userRepo.getUserById()?.email ?: throw Exception(NON_EXISTENT_USER)
                quizRepo.getQuizById(id)?.let { quiz ->
                    val newData = quiz.copy(
                        participants = getNewParticipantList(quiz, email, score, timeTaken)
                    )
                    quizRepo.updateQuiz(newData)
                    val results = Results(newData.questions.size, score, timeTaken)
                    _result.emit(results)
                    _submit.emit(
                        resourceProvider.getString(R.string.score_update)
                    )
                } ?: throw Exception(NON_EXISTENT_QUIZ)
            }
        }
    }
    private fun getNewParticipantList(
        quiz: Quiz, email: String, score: Int, timeTaken: Int
    ): List<Participant> {
        val participants = quiz.participants.toMutableList()
        val participant = Participant(email, score, timeTaken)
        val idx = participants.indexOfFirst { it.studentEmail == email }
        return participants.apply {
            if(idx != -1) this[idx] = participant
            else this.add(participant)
        }
    }
    fun getScore(answers: List<String>, correctAnswers: List<String>): Int {
        val results = mutableListOf<Boolean>()
        answers.forEachIndexed { index, answer -> results.add(correctAnswers[index] == answer) }
        return results.count { it }
    }
    fun getQuizId(): String? = quizId
}