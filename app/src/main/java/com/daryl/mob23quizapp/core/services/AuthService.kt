package com.daryl.mob23quizapp.core.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class AuthService {
    private val auth = FirebaseAuth.getInstance()
    suspend fun register(email: String, password: String): Boolean {
        val res = auth.createUserWithEmailAndPassword(email, password).await()
        return res.user != null
    }
    suspend fun login(email: String, password: String): FirebaseUser? {
        val res = auth.signInWithEmailAndPassword(email, password).await()
        return res.user
    }
    fun logout() = auth.signOut()
    fun getCurrentUser(): FirebaseUser? = auth.currentUser
    fun getUid(): String? = auth.currentUser?.uid
    fun isLoggedIn(): Boolean = auth.currentUser != null
}