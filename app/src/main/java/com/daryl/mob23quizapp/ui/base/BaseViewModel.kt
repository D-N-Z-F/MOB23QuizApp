package com.daryl.mob23quizapp.ui.base

import androidx.lifecycle.ViewModel
import com.daryl.mob23quizapp.data.models.Roles
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

abstract class BaseViewModel: ViewModel() {
    protected val _signIn = MutableSharedFlow<Pair<String, Roles>>()
    val signIn: SharedFlow<Pair<String, Roles>> = _signIn
    protected val _submit = MutableSharedFlow<String>()
    val submit: SharedFlow<String> = _submit
    private val _error = MutableSharedFlow<String>()
    val error: SharedFlow<String> = _error

    suspend fun <T> globalErrorHandler(func: suspend () -> T?): T? =
        try {
            func()
        } catch (e: Exception) {
            _error.emit(e.message.toString())
            e.printStackTrace()
            null
        }
}