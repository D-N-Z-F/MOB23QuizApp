package com.daryl.mob23quizapp.data.models

enum class Roles { TEACHER, STUDENT }

data class User(
    val username: String,
    val email: String,
    val role: Roles
)
