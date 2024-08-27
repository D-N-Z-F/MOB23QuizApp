package com.daryl.mob23quizapp.ui.loginRegister

import androidx.lifecycle.viewModelScope
import com.daryl.mob23quizapp.R
import com.daryl.mob23quizapp.core.Constants.EMAIL_ERROR_MESSAGE
import com.daryl.mob23quizapp.core.Constants.EMAIL_REGEX
import com.daryl.mob23quizapp.core.Constants.LOGIN
import com.daryl.mob23quizapp.core.Constants.NON_EXISTENT_USER
import com.daryl.mob23quizapp.core.Constants.PASSWORD_ERROR_MESSAGE
import com.daryl.mob23quizapp.core.Constants.PASSWORD_REGEX
import com.daryl.mob23quizapp.core.Constants.REGISTER
import com.daryl.mob23quizapp.core.Constants.REGISTRATION_FAILED
import com.daryl.mob23quizapp.core.Constants.USERNAME_ERROR_MESSAGE
import com.daryl.mob23quizapp.core.Constants.USERNAME_REGEX
import com.daryl.mob23quizapp.core.services.AuthService
import com.daryl.mob23quizapp.core.utils.ResourceProvider
import com.daryl.mob23quizapp.core.utils.Utils.capitalize
import com.daryl.mob23quizapp.data.models.User
import com.daryl.mob23quizapp.data.models.Validator
import com.daryl.mob23quizapp.data.repositories.UserRepo
import com.daryl.mob23quizapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginRegisterViewModel @Inject constructor(
    private val authService: AuthService,
    private val userRepo: UserRepo,
    private val resourceProvider: ResourceProvider
): BaseViewModel() {
    fun login(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            globalErrorHandler {
                authService.login(email, password) ?: throw Exception(NON_EXISTENT_USER)
                userRepo.getUserById()?.let {
                    _signIn.emit(
                        Pair(
                            resourceProvider.getString(
                                R.string.success_message, LOGIN.capitalize()
                            ),
                            it.role
                        )
                    )
                } ?: throw Exception(NON_EXISTENT_USER)
            }
        }
    }
    fun register(user: User, password: String, password2: String) {
        viewModelScope.launch(Dispatchers.IO) {
            globalErrorHandler {
                val error = performValidation(user, password, password2)
                if(error != null) throw Exception(error)
                val isRegistered = authService.register(user.email, password)
                if(!isRegistered) throw Exception(REGISTRATION_FAILED)
                userRepo.createUser(user)
                _signIn.emit(
                    Pair(
                        resourceProvider.getString(
                            R.string.success_message, REGISTER.capitalize()
                        ),
                        user.role
                    )
                )
            }
        }
    }
    private fun performValidation(
        user: User, password:String, password2: String
    ) = Validator.validate(
            Validator(user.username, USERNAME_REGEX, USERNAME_ERROR_MESSAGE),
            Validator(user.email, EMAIL_REGEX, EMAIL_ERROR_MESSAGE),
            Validator(password + password2, PASSWORD_REGEX, PASSWORD_ERROR_MESSAGE)
        )
}