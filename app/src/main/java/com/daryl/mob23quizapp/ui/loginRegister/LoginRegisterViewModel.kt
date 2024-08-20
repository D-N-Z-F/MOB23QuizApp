package com.daryl.mob23quizapp.ui.loginRegister

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.daryl.mob23quizapp.R
import com.daryl.mob23quizapp.core.Constants.LOGIN
import com.daryl.mob23quizapp.core.Constants.REGISTER
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
                authService.login(email, password) ?: throw Exception("User doesn't exist.")
            }?.let {
                _signIn.emit(
                    resourceProvider.getString(R.string.success_message, LOGIN.capitalize())
                )
            }
        }
    }
    fun register(user: User, password: String, password2: String) {
        viewModelScope.launch(Dispatchers.IO) {
            globalErrorHandler {
                val error = performValidation(user, password, password2)
                if(error != null) throw Exception(error)
                val isRegistered = authService.register(user.email, password)
                if(!isRegistered) throw Exception("Registration failed, please retry later.")
                userRepo.createUser(user)
            }?.let {
                _signIn.emit(
                    resourceProvider.getString(R.string.success_message, REGISTER.capitalize())
                )
            }
        }
    }
    private fun performValidation(user: User, password:String, password2: String) =
        Validator.validate(
            Validator(
                user.username,
                "[a-zA-Z ]{2,20}",
                "Username can only contain alphabets with a length of 2 to 20."
            ),
            Validator(
                user.email,
                "[a-zA-Z0-9]+@[a-zA-Z0-9]+.[a-zA-Z0-9]+",
                "Please enter a valid email. (e.g. johndoe123@gmail.com)"
            ),
            Validator(
                password + password2,
                "[a-zA-Z0-9#$%]{8,20}",
                "Password must have a length of 8 to 20, only (#$%) special characters are allowed."
            )
        )
}