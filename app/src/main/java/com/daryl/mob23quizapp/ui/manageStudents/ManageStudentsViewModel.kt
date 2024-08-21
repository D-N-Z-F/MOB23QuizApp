package com.daryl.mob23quizapp.ui.manageStudents

import com.daryl.mob23quizapp.data.models.User
import com.daryl.mob23quizapp.data.repositories.UserRepo
import com.daryl.mob23quizapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ManageStudentsViewModel @Inject constructor(
    private val userRepo: UserRepo
): BaseViewModel() {
    fun getAllStudents(): Flow<List<User>> = userRepo.getAllStudents()
}