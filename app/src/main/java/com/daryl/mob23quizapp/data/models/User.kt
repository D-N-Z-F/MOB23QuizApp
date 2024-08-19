package com.daryl.mob23quizapp.data.models

data class User(
    val username: String,
    val email: String,
    val role: Roles
) {
    companion object {
        fun fromMap(map: Map<*, *>): User =
            User(
                username = map["username"].toString(),
                email = map["email"].toString(),
                role = map["role"] as Roles
            )
    }
}
