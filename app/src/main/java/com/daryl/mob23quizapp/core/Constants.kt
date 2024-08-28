package com.daryl.mob23quizapp.core

object Constants {
    const val CLIP_ID = "CLIP_ID"

    const val USER_COLLECTION_PATH = "users"
    const val QUIZ_COLLECTION_PATH = "quizzes"

    const val INFO = "INFO"
    const val SUCCESS = "SUCCESS"
    const val ERROR = "ERROR"

    const val LOGIN = "LOGIN"
    const val LOGOUT = "LOGOUT"
    const val REGISTER = "REGISTER"
    const val PASSWORD = "Password"
    const val ADD = "ADD"
    const val EDIT = "EDIT"
    const val DELETE = "DELETE"

    const val HOLDER_TYPE_1 = 1
    const val HOLDER_TYPE_2 = 2

    const val PARTICIPANTS = "PARTICIPANTS"
    const val QUESTIONS = "QUESTIONS"

    const val UNEXPECTED_ERROR = "Unexpected error occurred."
    const val NON_EXISTENT_USER = "User doesn't exist."
    const val REGISTRATION_FAILED = "Registration failed, please retry later."
    const val NON_EXISTENT_QUIZ = "Quiz doesn't exist."
    const val EMPTY_QUIZ_ID = "Please enter a valid Quiz ID."
    const val NON_EXISTENT_CSV = "Please upload a valid CSV file."

    const val DEFAULT_TIME_PER_QUESTION = 10
    const val COUNT_DOWN_INTERVAL = 1000L
    const val LOAD_DELAY_TIMING = 1500L
    const val SPLASH_SCREEN_DURATION = 2500L

    const val BLUE = "BLUE"
    const val WHITE = "WHITE"
    const val BLACK = "BLACK"

    const val USERNAME_REGEX = "[a-zA-Z ]{2,20}"
    const val USERNAME_ERROR_MESSAGE = "Username can only contain alphabets with a length of 2 to 20."
    const val EMAIL_REGEX = "[a-zA-Z0-9]+@[a-zA-Z0-9]+.[a-zA-Z0-9]+"
    const val EMAIL_ERROR_MESSAGE = "Please enter a valid email. (e.g. johndoe123@gmail.com)"
    const val PASSWORD_REGEX = "[a-zA-Z0-9#$%]{8,20}"
    const val PASSWORD_ERROR_MESSAGE = "Password must have a length of 8 to 20, only (#$%) special characters are allowed."
    const val PASSWORD_MISMATCH = "Both passwords must match!"
    const val QUIZ_NAME_REGEX = "[a-zA-Z ]{2,20}"
    const val QUIZ_NAME_ERROR_MESSAGE = "Name can only contain alphabets with a length of 2 to 20."
    const val QUIZ_CATEGORY_REGEX = "[a-zA-Z ]{2,20}"
    const val QUIZ_CATEGORY_ERROR_MESSAGE = "Category can only contain alphabets with a length of 2 to 20."
}