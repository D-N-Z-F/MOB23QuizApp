package com.daryl.mob23quizapp.ui.addQuiz

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.daryl.mob23quizapp.R
import com.daryl.mob23quizapp.core.Constants.ADD
import com.daryl.mob23quizapp.core.services.StorageService
import com.daryl.mob23quizapp.core.utils.ResourceProvider
import com.daryl.mob23quizapp.core.utils.Utils.capitalize
import com.daryl.mob23quizapp.core.utils.Utils.debugLog
import com.daryl.mob23quizapp.data.models.Question
import com.daryl.mob23quizapp.data.models.Quiz
import com.daryl.mob23quizapp.data.models.Validator
import com.daryl.mob23quizapp.data.repositories.QuizRepo
import com.daryl.mob23quizapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
                val error = performValidation(quiz)
                if(error != null) throw Exception(error)
                val data = quiz.copy(questions = questions)
                if(data.questions.isEmpty())
                    throw Exception("Unable to create empty quiz! Please upload a valid CSV file.")
                quizRepo.createQuiz(data)
                    ?: throw Exception("Unexpected error occurred while adding, please retry later.")
            }?.let {
                _submit.emit(
                    resourceProvider.getString(R.string.success_message, ADD.capitalize())
                )
            }
        }
    }
    fun getQuestionsFromCSV(uri: Uri) { questions = storageService.parseCSVFile(uri) }
    fun getFileName(uri: Uri): String? = storageService.getFileName(uri)
    private fun performValidation(quiz: Quiz): String? =
        Validator.validate(
            Validator(
                quiz.name,
                "[a-zA-Z ]{2,20}",
                "Name can only contain alphabets with a length of 2 to 20."
            ),
            Validator(
                quiz.category,
                "[a-zA-Z ]{2,20}",
                "Category can only contain alphabets with a length of 2 to 20."
            )
        )
}