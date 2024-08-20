package com.daryl.mob23quizapp.data.models

data class Question(
    val id: Int,
    val title: String,
    val options: List<String>,
    val answer: String
)
