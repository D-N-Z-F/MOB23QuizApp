package com.daryl.mob23quizapp.data.models

data class Participant(
    val studentId: Int,
    val score: Int,
    val timeTaken: Int
) {
    companion object {
        fun fromMap(map: Map<*, *>): Participant =
            Participant(
                studentId = map["studentId"].toString().toInt(),
                score = map["score"].toString().toInt(),
                timeTaken = map["timeTaken"].toString().toInt()
            )
    }
}