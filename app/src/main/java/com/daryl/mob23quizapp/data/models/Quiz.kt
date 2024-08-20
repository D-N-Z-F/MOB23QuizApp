package com.daryl.mob23quizapp.data.models

data class Quiz(
    val id: Int,
    val name: String,
    val category: String,
    val questions: List<Question>,
    val timePerQuestion: Int
)