package com.daryl.mob23quizapp.data.models

data class Participant(
    val studentEmail: String,
    val score: Int,
    val timeTaken: Int
) {
    companion object {
        fun fromMap(map: Map<*, *>): Participant =
            Participant(
                studentEmail = map["studentEmail"].toString(),
                score = map["score"].toString().toInt(),
                timeTaken = map["timeTaken"].toString().toInt()
            )
    }
}