package com.daryl.mob23quizapp.data.models

data class Question(
    val title: String,
    val options: List<String>,
    val answer: String
) {
    companion object {
        fun fromMap(map: Map<*, *>): Question =
            Question(
                title = map["title"].toString(),
                options = (map["options"] as? List<*>)?.map { it.toString() } ?: emptyList(),
                answer = map["answer"].toString()
            )
    }
}
