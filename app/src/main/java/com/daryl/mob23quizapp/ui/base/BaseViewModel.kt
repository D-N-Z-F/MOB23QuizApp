package com.daryl.mob23quizapp.ui.base

import androidx.lifecycle.ViewModel
import com.daryl.mob23quizapp.data.models.Roles
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

abstract class BaseViewModel: ViewModel() {
    protected val _finish = MutableSharedFlow<String>()
    val finish: SharedFlow<String> = _finish
    protected val _signIn: MutableSharedFlow<String> = MutableSharedFlow()
    val signIn: SharedFlow<String> = _signIn
    protected val _submit: MutableSharedFlow<String> = MutableSharedFlow()
    val submit: SharedFlow<String> = _submit
    protected val _error = MutableSharedFlow<String>()
    val error: SharedFlow<String> = _error
    protected val _role = MutableSharedFlow<Roles>()
    val role: SharedFlow<Roles> = _role

    suspend fun <T> globalErrorHandler(func: suspend () -> T?): T? =
        try {
            func()
        } catch (e: Exception) {
            _error.emit(e.message.toString())
            e.printStackTrace()
            null
        }
    suspend fun triggerMenuSetup(role: Roles) { _role.emit(role) }
}