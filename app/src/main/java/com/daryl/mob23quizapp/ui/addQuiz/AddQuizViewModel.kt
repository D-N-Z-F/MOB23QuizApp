package com.daryl.mob23quizapp.ui.addQuiz

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.daryl.mob23quizapp.R
import com.daryl.mob23quizapp.core.Constants.ADD
import com.daryl.mob23quizapp.core.Constants.LOAD_DELAY_TIMING
import com.daryl.mob23quizapp.core.Constants.NON_EXISTENT_CSV
import com.daryl.mob23quizapp.core.Constants.QUIZ_CATEGORY_ERROR_MESSAGE
import com.daryl.mob23quizapp.core.Constants.QUIZ_CATEGORY_REGEX
import com.daryl.mob23quizapp.core.Constants.QUIZ_NAME_ERROR_MESSAGE
import com.daryl.mob23quizapp.core.Constants.QUIZ_NAME_REGEX
import com.daryl.mob23quizapp.core.Constants.UNEXPECTED_ERROR
import com.daryl.mob23quizapp.core.services.StorageService
import com.daryl.mob23quizapp.core.utils.ResourceProvider
import com.daryl.mob23quizapp.core.utils.Utils.capitalize
import com.daryl.mob23quizapp.data.models.Question
import com.daryl.mob23quizapp.data.models.Quiz
import com.daryl.mob23quizapp.data.models.Validator
import com.daryl.mob23quizapp.data.repositories.QuizRepo
import com.daryl.mob23quizapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddQuizViewModel @Inject constructor(
    private val quizRepo: QuizRepo,
    private val storageService: StorageService,
    private val resourceProvider: ResourceProvider
): BaseViewModel() {
    private var questions: List<Question> = emptyList()
    fun add(quiz: Quiz) {
        viewModelScope.launch(Dispatchers.IO) {
            globalErrorHandler {
                delay(LOAD_DELAY_TIMING)
                val error = performValidation(quiz)
                if(error != null) throw Exception(error)
                val data = quiz.copy(questions = questions)
                if(data.questions.isEmpty()) throw Exception(NON_EXISTENT_CSV)
                quizRepo.createQuiz(data)?.let {
                    _submit.emit(
                        resourceProvider.getString(
                            R.string.success_message, ADD.capitalize()
                        )
                    )
                } ?: throw Exception(UNEXPECTED_ERROR)
            }
        }
    }
    fun getQuestionsFromCSV(uri: Uri) { questions = storageService.parseCSVFile(uri) }
    fun getFileName(uri: Uri): String? = storageService.getFileName(uri)
    private fun performValidation(quiz: Quiz): String? =
        Validator.validate(
            Validator(quiz.name, QUIZ_NAME_REGEX, QUIZ_NAME_ERROR_MESSAGE),
            Validator(quiz.category, QUIZ_CATEGORY_REGEX, QUIZ_CATEGORY_ERROR_MESSAGE)
        )
}