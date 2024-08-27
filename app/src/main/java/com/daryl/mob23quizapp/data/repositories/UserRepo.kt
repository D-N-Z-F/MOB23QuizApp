package com.daryl.mob23quizapp.data.repositories

import com.daryl.mob23quizapp.core.services.AuthService
import com.daryl.mob23quizapp.data.models.Roles
import com.daryl.mob23quizapp.data.models.User
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class UserRepo(
    private val authService: AuthService
) {
    private fun getUid(): String = authService.getUid() ?: throw Exception("User doesn't exist.")
    private fun getCollection(): CollectionReference =
        Firebase.firestore.collection("users")
    fun getAllStudents() = callbackFlow<List<User>> {
        val listener = getCollection().addSnapshotListener { value, error ->
            if(error != null) throw error
            val students = mutableListOf<User>()
            value?.documents?.map { snapshot ->
                snapshot?.data?.let { map ->
                    val student = User.fromMap(map)
                    if(student.role == Roles.STUDENT) students.add(student)
                }
            }
            trySend(students)
        }
        awaitClose { listener.remove() }
    }
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