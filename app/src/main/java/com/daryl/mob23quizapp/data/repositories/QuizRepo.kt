package com.daryl.mob23quizapp.data.repositories

import com.daryl.mob23quizapp.core.services.AuthService
import com.daryl.mob23quizapp.core.utils.Utils.debugLog
import com.daryl.mob23quizapp.data.models.Quiz
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class QuizRepo(
    private val authService: AuthService
) {
    private fun getUid(): String = authService.getUid() ?: throw Exception("User doesn't exist.")
    private fun getCurrentUser(): FirebaseUser = authService.getCurrentUser() ?: throw Exception("User doesn't exist.")
    private fun getCollection(): CollectionReference =
        Firebase.firestore.collection("quizzes")
    fun getTeacherQuizzes() = callbackFlow<List<Quiz>> {
        val listener = getCollection().addSnapshotListener { value, error ->
            if(error != null) throw error
            val quizzes = mutableListOf<Quiz>()
            value?.documents?.map { snapshot ->
                snapshot.data?.let { map ->
                    val quiz = Quiz.fromMap(map)
                    if(quiz.teacherId == getUid()) quizzes.add(quiz.copy(id = snapshot.id))
                }
            }
            trySend(quizzes)
        }
        awaitClose { listener.remove() }
    }
    fun getStudentQuizHistory() = callbackFlow<List<Quiz>> {
        val listener = getCollection().addSnapshotListener { value, error ->
            if(error != null) throw error
            val history = mutableListOf<Quiz>()
            value?.documents?.map { snapshot ->
                snapshot.data?.let { map ->
                    val quiz = Quiz.fromMap(map)
                    if(quiz.participants.any { it.studentEmail == getCurrentUser().email }) {
                        history.add(quiz.copy(id = snapshot.id))
                    }
                }
            }
            trySend(history)
        }
        awaitClose { listener.remove() }
    }
    suspend fun createQuiz(quiz: Quiz): String? {
        val data = quiz.copy(teacherId = getUid())
        val res = getCollection().add(data.toMap()).await()
        return res?.id
    }
    suspend fun updateQuiz(quiz: Quiz) {
        getCollection().document(quiz.id!!).set(quiz.toMap()).await()
    }
    suspend fun deleteQuiz(id: String) {
        getCollection().document(id).delete().await()
    }
    suspend fun getQuizById(id: String): Quiz? {
        val res = getCollection().document(id).get().await()
        return res.data?.let { Quiz.fromMap(it).copy(id = res.id) }
    }
}