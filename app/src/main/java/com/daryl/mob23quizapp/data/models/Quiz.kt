package com.daryl.mob23quizapp.data.models

import com.daryl.mob23quizapp.core.utils.Utils.debugLog

data class Quiz(
    val id: String? = null,
    val name: String,
    val category: String,
    val questions: List<Question>,
    val timePerQuestion: Int,
    val participants: List<Participant> = emptyList()
) {
    fun toMap(): Map<String, Any?> =
        mapOf(
            "name" to name,
            "category" to category,
            "questions" to questions,
            "timePerQuestion" to timePerQuestion,
            "participants" to participants
        )
    companion object {
        private inline fun <reified T> parseListType(list: List<*>): List<T> =
            when {
                T::class == Question::class ->
                    list.map { Question.fromMap(it as Map<*, *>) as T }
                T::class == Participant::class ->
                    list.map { Participant.fromMap(it as Map<*, *>) as T }
                else -> emptyList()
            }
        fun fromMap(map: Map<*, *>): Quiz =
            Quiz(
                name = map["name"].toString(),
                category = map["category"].toString(),
                questions = (map["questions"] as? List<*>)?.let {
                    parseListType<Question>(it)
                } ?: emptyList(),
                timePerQuestion = map["timePerQuestion"].toString().toInt(),
                participants = (map["participants"] as? List<*>)?.let {
                    parseListType<Participant>(it)
                } ?: emptyList()
            )
    }
}