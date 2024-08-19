package com.daryl.mob23quizapp.data.repositories

import com.daryl.mob23quizapp.core.services.AuthService
import com.daryl.mob23quizapp.data.models.User
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class UserRepo(
    private val authService: AuthService
) {
    private fun getUid(): String = authService.getUid() ?: throw Exception("User doesn't exist!")
    private fun getCollection(): CollectionReference =
        Firebase.firestore.collection("users")
    suspend fun createUser(user: User) {
        getCollection().document(getUid()).set(user).await()
    }
    suspend fun updateUser(user: User) {
        getCollection().document(getUid()).set(user).await()
    }
    suspend fun getUserById(): User? {
        val res = getCollection().document(getUid()).get().await()
        return res.data?.let { User.fromMap(it) }
    }
}